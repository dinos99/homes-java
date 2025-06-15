package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

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
	
	public final String BTJOB_READY_SPLIT_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT000 + File.separator + "split" ; 
	public final String BTJOB_READY_BDT001_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT001 ; 
	
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
		File wait_dir  = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ; 
		File bdt001_dir = new File( BTJOB_READY_BDT001_PATH ) ; 
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !split_dir.isDirectory()) split_dir.mkdirs() ; 
		if ( !bdt001_dir.isDirectory()) bdt001_dir.mkdirs() ; 
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
	
	public CommonMap parseLine(String rowdata, long no) {
		CommonMap pMap = new CommonMap() ;
		String[] datas = rowdata.split("[|]") ;
		
		pMap.put("no"  , no) ; 
		pMap.put("uuid", this.bt_uuid) ;
		
		/* 필요한것들만 가져오자 */
		pMap.put("mngrRegstrPk", datas[ 0]) ;
		pMap.put("regstrGbCd"  , datas[ 2]) ;
		pMap.put("regstrKdCd"  , datas[ 4]) ;
		pMap.put("arcd"        , datas[ 9]) ;
		pMap.put("legcd"       , datas[10]) ;
		pMap.put("bun"         , datas[12]) ;
		pMap.put("ji"          , datas[13]) ;
		pMap.put("regstrCrde"  , datas[29]) ;

		pMap.put("rawdata"     , rowdata)   ; 
		
		return pMap ;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int insertRawData(File jbfile) {
		int ins_co = 0 ; 
		FileReader fr = null ;
		BufferedReader br = null ;
		
		Long no = 1l ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			while((line = br.readLine()) != null ) {
				CommonMap pMap = parseLine(line, no) ;
				ins_co += mapper.insertBaseSummryRawData(pMap) ;	
				if ((no % SPLIT_LINE) == 0) {
					Log.info("inserted {} Lines", StringUtil.getCurrencyFormat(no)) ;
				}
				no ++ ; 
			}
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
        	Log.error("*** job finished: {}/{}:", ins_co, System.currentTimeMillis()) ; 
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		return ins_co ; 
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
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT000) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", "[건축물 관리대장]기본개요 원시데이터를 생성합니다.") ;
		mapper.insertBatchjob(btmap) ;
	}

	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, long exco, String message) {
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , exco) ;
		btmap.put("filenm" , FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", message) ;
		mapper.updateBatchjob(btmap) ;
	}
	public BatchVo doExecute(String batchYn) {
		create_jobdir() ;
		String message = "" ;
		int ins_co = 0 ;

		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ; 
		if ( split_dir.isDirectory() && split_dir.list().length > 0) {
			String[] f_nm = split_dir.list() ; 
			for ( int i = 0; i < f_nm.length; i ++ ) {
				/* 일단 하나만 이동 */ 
				File source = new File( BTJOB_READY_SPLIT_PATH  + File.separator + f_nm[i]) ; 
				File dest   = new File( BTJOB_READY_BDT001_PATH + File.separator + f_nm[i]) ;
				source.renameTo(dest) ;
				
				this.bt_uuid = getUUID(f_nm[i]) ;
				ready() ;
				ins_co = insertRawData(dest) ;
				message = "기본개요 등록완료(" + StringUtil.getCurrencyFormat(ins_co) + ")" ; 
				done(BTS_DONE, ins_co, message) ;
				Log.info("**** job file {}/{}", i + 1, split_dir.list().length) ;
			}
		} else {
			message = "작업대상파일이 없습니다." ;
		}
		
		BatchVo btVo = new BatchVo(BDT001, batchYn) ;
		btVo.setExco(ins_co);
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);
		
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
