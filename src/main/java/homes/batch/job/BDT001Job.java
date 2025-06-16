package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumBatchJob;
import homes.comm.constants.EnumError;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.HomesProperty;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BDT001Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT001Job.class) ;

	private final BatchMapper mapper ; 
	
	public final String BDT000 = EnumBatchJob.SPLIT_BASE_SUMMRY_RAWDATA.getCode() ; 
	public final String BDT001 = EnumBatchJob.INSERT_BASE_SUMMRY_RAWDATA.getCode() ; 
	
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT000  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT000  ; 
	
	public final String BTJOB_READY_SPLIT_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT000 + File.separator + "split" ; 
	public final String BTJOB_READY_BDT001_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT001 ; 
	public final String BTJOB_DONE_BDT001_PATH  = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT001 ; 
	
	public final String FILE_PRIFIX = "ready-" ; 
	public final String FILE_EXTENTION = ".txt" ; 

	public final String BTS_PROC  = "BTS001" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_DONE  = "BTS002" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_ERROR = "BTS999" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 

	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String ORIGIN_FILE_PREFIX = "ORIGIN-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 
	
	public final int SPLIT_LINE = 1000;
	
	public String bt_uuid = "" ; 

	public String getUUID( String f_nm ) {
		this.bt_uuid = f_nm.substring(10, 46) ;
		return this.bt_uuid ; 
	}
	
	public void create_jobdir() {
		File wait_dir   = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir  = new File( BTJOB_READY_PATH ) ; 
		File split_dir  = new File( BTJOB_READY_SPLIT_PATH ) ; 
		File bdt001_dir = new File( BTJOB_READY_BDT001_PATH ) ; 
		File done_dir   = new File( BTJOB_DONE_BDT001_PATH ) ; 
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !split_dir.isDirectory()) split_dir.mkdirs() ; 
		if ( !bdt001_dir.isDirectory()) bdt001_dir.mkdirs() ; 
		if ( !done_dir.isDirectory()) done_dir.mkdirs() ; 
	}
	
	public void move_done(File source) {
		File dest = new File( BTJOB_DONE_BDT001_PATH + File.separator + source.getName()) ; 
		source.renameTo(dest) ;
	}
	
	public String[] getHeader() {
		String[] headers = {
			"mngrRegstrPk", "upperRegstrPk", "regstrGbCd", "regstrGbNm", "regstrKdCd", "regstrKdNm", "plotLoc", "rdnmPlotLoc", "buldNm"
			, "arcd", "legcd", "plotGbCd", "bun", "ji"
			, "spPlotNm", "block", "lot", "outLotCo", "nwAddrRoadCd", "nwAddrLegCd", "nwAddrGrndCd", "nwAddrMainB", "nwAddrSubB"
			, "zoneCd", "districtCd", "areaCd"
			, "zoneCdNm", "districtCdNm", "areaCdNm"
			, "creatDe"
		} ; 
		return headers ; 
	}
	
	public CommonMap parseLine(String rowdata) {
		CommonMap pMap = new CommonMap() ;
		String[] datas = rowdata.split("[|]") ;
		
		/* 필요한것들만 가져오자 */
		pMap.put("mngrRegstrPk", datas[ 0]) ;
		pMap.put("upperRegstrPk",datas[ 1]) ;
		pMap.put("regstrGbCd"  , datas[ 2]) ;
		pMap.put("regstrKdCd"  , datas[ 4]) ;
		pMap.put("arcd"        , datas[ 9]) ;
		pMap.put("legcd"       , datas[10]) ;
		pMap.put("bun"         , datas[12]) ;
		pMap.put("ji"          , datas[13]) ;
		pMap.put("regstrCrde"  , datas[29]) ;
		return pMap ;
	}
	
	@Transactional
	public CommonMap insertBaseSummryData(File jbfile) {
		int ins_bass_summry = 0 ; 
		int ins_ag_buld    = 0 ; 
		int ins_buld        = 0 ; 
		int ins_pssion_buld = 0 ;
		
		CommonMap inmap = new CommonMap() ;
		
		FileReader fr = null ;
		BufferedReader br = null ;
		int no = 1 ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			while((line = br.readLine()) != null ) {
				CommonMap pMap = parseLine(line) ;
				
				String arcd = Optional.ofNullable((String)pMap.get("arcd")).orElse("") ;
				arcd = "".equals(arcd) ? "00000" : arcd ; 
				String gbcd = Optional.ofNullable((String)pMap.get("regstrGbCd")).orElse("") ;
				String kdcd = Optional.ofNullable((String)pMap.get("regstrKdCd")).orElse("") ;
//				Log.info("arcd: {}, gbcd: {}, kdcd: {}", arcd, gbcd, kdcd) ;
				if ( !"00000".equals(arcd) && arcd.length() == 5 ) {
					if ( !"4".equals(kdcd)) {
						ins_bass_summry += mapper.insertHbdBaseSummry(pMap) ;
						/* 전유부 제외 */
						if ( "1".equals(gbcd)) {  
							ins_buld += mapper.insertHbdBuld(pMap) ;
						} else if ("2".equals(gbcd)) {
							ins_ag_buld += mapper.insertHbdAgBuld(pMap) ;
						} else {
							Log.error("*** Data error, regstrGbCd: {}", gbcd) ;
						}
					} else {
						try {
							ins_pssion_buld += mapper.insertHbdPssionBuld(pMap) ;
						} catch( SQLException e ) {
							Log.error("*** Errlor: {}", e.getMessage());
							continue ;
						}
					}
					if ((no % SPLIT_LINE) == 0) {
						Log.info("inserted {} Lines", StringUtil.getCurrencyFormat(no)) ;
						Log.error("***************************************************") ;
			        	Log.error("*** inserted bass_summry: {}", StringUtil.getCurrencyFormat(ins_bass_summry));
			        	Log.error("*** inserted build      : {}", StringUtil.getCurrencyFormat(ins_buld));
			        	Log.error("*** inserted ag_build   : {}", StringUtil.getCurrencyFormat(ins_ag_buld));
			        	Log.error("*** inserted pssion_buld: {}", StringUtil.getCurrencyFormat(ins_pssion_buld));
						Log.error("***************************************************") ;
					}
					no ++ ; 
				}
			}
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
			Log.error("***************************************************") ;
        	Log.error("*** job finished at: {} ", DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
			Log.error("***************************************************") ;
        	Log.error("*** inserted bass_summry: {}", ins_bass_summry);
        	Log.error("*** inserted build      : {}", ins_buld);
        	Log.error("*** inserted ag_build   : {}", ins_ag_buld);
        	Log.error("*** inserted pssion_buld: {}", ins_pssion_buld);
			Log.error("***************************************************") ;
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		inmap.put("ins_bass_summry", ins_bass_summry) ; 
		inmap.put("ins_buld"       , ins_buld) ; 
		inmap.put("ins_ag_buld"    , ins_ag_buld) ; 
		inmap.put("ins_pssion_buld", ins_pssion_buld) ; 
		inmap.put("ex_co"          , no - 1) ; 
		
		return inmap ; 
	}
		
	@Transactional(rollbackFor = Exception.class)
	public void deleteRawData() {
		mapper.deleteBaseSummryRawdata(this.bt_uuid) ; 
	}
	
	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready() {
		CommonMap btmap = new CommonMap() ;
		this.bt_uuid = UUID.randomUUID().toString() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT000) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", "[건축물 관리대장]기본개요 데이터를 생성합니다.") ;
		mapper.insertBatchjob(btmap) ;
	}

	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, int ex_co, String message) {
		Log.info("*** uuid: {}", this.bt_uuid) ; 
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , ex_co) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", message) ;
		mapper.updateBatchjob(btmap) ;
	}
	public BatchVo doExecute(String batchYn) {
		create_jobdir() ;
		int ex_co =0 ; 
		String message = "" ;
		CommonMap inmap = null ; 

		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ; 
		if ( split_dir.isDirectory() && split_dir.list().length > 0) {
			ready() ;
			String[] f_nm = split_dir.list() ; 
			for ( int i = 0; i < f_nm.length; i ++ ) {
				File source = new File( BTJOB_READY_SPLIT_PATH  + File.separator + f_nm[i]) ; 
				File dest   = new File( BTJOB_READY_BDT001_PATH + File.separator + f_nm[i]) ;
				source.renameTo(dest) ;
				inmap = insertBaseSummryData(dest) ;
				Log.info("**** job file {}/{}", i + 1, f_nm.length) ;
				move_done(dest) ;
			}

			int ins_bass_summry = StringUtil.getIntValue(inmap, "ins_bass_summry") ; 
			int ins_buld        = StringUtil.getIntValue(inmap, "ins_buld") ; 
			int ins_ag_buld     = StringUtil.getIntValue(inmap, "ins_ag_buld") ; 
			int ins_pssion_buld = StringUtil.getIntValue(inmap, "ins_pssion_buld") ;
			
			ex_co = StringUtil.getIntValue(inmap, "ex_co") ;
			
			message = "" ; 
			message = message + "[기본개요] 등록완료 ( " ;
			message = message + StringUtil.getCurrencyFormat(ins_bass_summry) + " / " ;
			message = message + StringUtil.getCurrencyFormat(ins_buld) + " / " ;
			message = message + StringUtil.getCurrencyFormat(ins_ag_buld) + " / " ;
			message = message + StringUtil.getCurrencyFormat(ins_pssion_buld) + " / " ;
			message = message + StringUtil.getCurrencyFormat(ex_co) + " )" ;
			
			done(BTS_DONE, ex_co, message) ;
		} else {
			message = "작업대상파일이 없습니다." ;
		}
		
		BatchVo btVo = new BatchVo(BDT001, batchYn) ;
		btVo.setExco(ex_co);
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);

		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BDT001, ex_co, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		return btVo ; 
	}
	
	/** *********************************************************
	 * 건축물관리대장 > 기본개요 > RAWDATA INSERT
	 ** ********************************************************/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
        String batchYn     = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batchYn ) ;
		doExecute(batchYn) ; 
	}
}
