package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class BDT021Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT021Job.class) ;

	private final BatchMapper mapper ; 
	
	public final String BDT021 = EnumBatchJob.INSERT_TOTAL_LEDGER.getCode() ; 
	
	public final String BTJOB_WAIT_BDT021_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT021  ; 
	public final String BTJOB_READY_BDT021_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT021  ; 
	public final String BTJOB_DONE_BDT021_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT021  ; 

	public final String FILE_READY_PREFIX = "ready-" ; 
	public final String FILE_DONE_PREFIX  = "done-" ; 
	public final String FILE_EXTENTION = ".txt" ; 

	public final String BTS_PROC  = EnumBatchJob.BTS_PROC.getCode() ;  
	public final String BTS_DONE  = EnumBatchJob.BTS_DONE.getCode() ;  
	public final String BTS_ERROR = EnumBatchJob.BTS_ERROR.getCode() ;  

	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 
	
	public final int SPLIT_LINE = 1000;
	
	public String bt_uuid = "" ; 
	public String batchYn = "" ; 

	public String getUUID() {
		this.bt_uuid = UUID.randomUUID().toString() ; 
		return this.bt_uuid ; 
	}
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_BDT021_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_BDT021_PATH ) ; 
		File done_dir  = new File( BTJOB_DONE_BDT021_PATH ) ; 

		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !done_dir.isDirectory()) done_dir.mkdirs() ; 
	}
	
	public boolean is_exists_file(String path) {
		File dir = new File( path ) ;
		return ( dir.isDirectory() && dir.list().length > 0 ) ;
	}
	
	public File move_ready() {
		File wait_dir  = new File(BTJOB_WAIT_BDT021_PATH) ; 
		File ready_dir = new File(BTJOB_READY_BDT021_PATH) ;
		String f_nm = FILE_READY_PREFIX + BDT021  + "-" + this.bt_uuid + FILE_EXTENTION ; 
		File dest   = new File( ready_dir.getAbsolutePath() + File.separator + f_nm) ;
		if ( wait_dir.isDirectory() && wait_dir.list().length > 0 ) {
			Log.info("wait  directory path: {}", wait_dir.getAbsolutePath()) ; 
			Log.info("ready directory path: {}", ready_dir.getAbsolutePath()) ; 
			File source = new File( wait_dir.getAbsolutePath()  + File.separator + wait_dir.list()[0] ) ;
			source.renameTo(dest) ;
		}
		return dest ; 
	}
	public void move_done(File source) {
		String f_nm = FILE_DONE_PREFIX + BDT021 + "-" + this.bt_uuid + FILE_EXTENTION ;
		File dest = new File( BTJOB_DONE_BDT021_PATH + File.separator + f_nm) ; 
		source.renameTo(dest) ;
		Log.info("File move done directory: {}", dest.getAbsolutePath()) ;
	}
	
	public String[] getHeader() {
		String[] headers = {
			"buldRegstrPk", "regstrGbCd", "regstrGbNm", "regstrKdCd", "regstrKdNm", "nwOdGbCd", "nwOdGbNm", "plotLoc", "rdnmPlotLoc", "buldNm"
			, "arcd", "legcd", "plotGbCd", "bun", "ji", "spPlotNm", "block", "lot", "outLotCo", "nwAddrRoadCd"
			, "nwAddrLegCd", "nwAddrGndCd", "nwAddrMstB", "nwAddrSubB", "plotAr", "buldAr", "buldLndRt", "totalAr", "bulkCalcTotAr", "bulkRt"
			, "mainPpsCd","mainPpsCdNm", "etcPps", "hshldCo", "fmlyCo", "mainBuldCo", "subBuldCo", "subBuldAr", "totParkngCo", "inMechaCo"
			, "inMechaAr", "outMechaCo", "outMechaAr", "inIndpntCo", "inIndpntAr", "outIndpntCo", "outIndpntAr", "prmissDe", "stwkDe", "useConfDe"
			, "prmissNoYyyy", "prmissNoOrgCd", "prmissNoOrgNm", "prmissNoGbCd", "prmissNoGbNm", "unitCo", "energyEfcnyGrad", "energyRedcnRt", "energyEpiScr", "ecoBuldGrad"
			, "ecoBuldScr", "brinBuldGrad", "brinBuldScr", "creatDe"
		} ; 
		return headers ; 
	}
	
	public CommonMap parseLine(String rowdata) {
		CommonMap pMap = new CommonMap() ;
		String[] cols  = this.getHeader() ;
		String[] datas = rowdata.split("[|]") ;
		
		for ( int i = 0; i < datas.length; i ++ ) {
			String column = cols[i] ; 
			String data   = datas[i] ;

			if ("outLotCo".equals(column) || "hshldCo".equals(column) || "fmlyCo".equals(column) || "mainBuldCo".equals(column) 
				|| "subBuldCo".equals(column) || "totParkngCo".equals(column) || "inMechaCo".equals(column) || "outMechaCo".equals(column) 
				|| "inIndpntCo".equals(column) || "outIndpntCo".equals(column) || "hoCo".equals(column) 
				 ||"nwAddrMstB".equals(column) || "nwAddrSubB".equals(column)) {
				int iVal = StringUtil.getIntValue(data) ;
				pMap.put(column, iVal) ;
			} else if ( "subBuldAr".equals(column) || "plotAr".equals(column) || "buldAr".equals(column) || "buldLndRt".equals(column)
						|| "totalAr".equals(column) || "bulkCalcTotAr".equals(column) || "bulkRt".equals(column) || "inMechaAr".equals(column)
						|| "outMechaAr".equals(column) || "inIndpntAr".equals(column) || "outIndpntAr".equals(column) || "energyRedcnRt".equals(column)
						|| "energyEpiScr".equals(column) || "ecoBuldScr".equals(column) || "brinBuldScr".equals(column)) {
				float fVal = StringUtil.getFloatValue(data) ; 
				pMap.put(column, fVal) ;
			} else {
				pMap.put(column, data) ;
			} 
		}
		pMap.put("batchYn", this.batchYn) ;
		return pMap ;
	}
	
	@Transactional
	public Map<String, Object> insertTotalLedger(File jbfile) {
		Long ins_co = 0l ;
		Map<String, Object> insmap = new HashMap<String, Object>() ;
		
		FileReader fr = null ;
		BufferedReader br = null ;
		int no = 1 ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
//			mapper.truncateTotalLedger() ; 
			while((line = br.readLine()) != null ) {
				CommonMap pMap = parseLine(line) ;
				
				String arcd = Optional.ofNullable((String)pMap.get("arcd")).orElse("00000") ;
//				Log.info("arcd: {}, gbcd: {}, kdcd: {}", arcd, gbcd, kdcd) ;
				if ( !"00000".equals(arcd) && arcd.length() == 5 ) {
					ins_co += mapper.insertTotalTitleLedgr(pMap) ;
					if ((no % SPLIT_LINE) == 0) {
						Log.info("file {}, execute count: {}", jbfile.getName(), StringUtil.getCurrencyFormat(no)) ;
						Log.error("***************************************************") ;
			        	Log.error("*** inserted total Ledger: {}", StringUtil.getCurrencyFormat(ins_co));
						Log.error("***************************************************") ;
					}
					no ++ ; 
				}
			}
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
        	Log.error("*** job {} finished at: {} ", jbfile.getName(), DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
        	move_done(jbfile) ; 
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		insmap.put("ins_co" , ins_co) ; 
		insmap.put("message", "[건축물관리대장]총괄표제부 등록(" + StringUtil.getCurrencyFormat(ins_co) + "건)") ;
		return insmap ; 
	}
	
	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready() {
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT021) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", "[건축물 관리대장]총괄표제부 데이터를 생성합니다.") ;
		mapper.insertBatchjob(btmap) ;
	}

	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, double ex_co, String message) {
		Log.info("*** uuid: {}", this.bt_uuid) ; 
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , ex_co) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", message) ;
		mapper.updateBatchjob(btmap) ;
	}
	public BatchVo doExecute(String batYn) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = batYn ; 
		Map<String, Object> insmap = null ; 
		create_jobdir() ;
		Long ins_co = 0l ; 
		String message = "" ; 
		boolean is_wait = is_exists_file(BTJOB_WAIT_BDT021_PATH) ;
		
		if ( !is_wait) {
			message = "[대기]디렉토리에 작업파일이 존재하지 않습니다." ; 
			BatchVo btVo = new BatchVo(BDT021, batchYn) ;
			btVo.setExco(ins_co) ; 
			btVo.setBatchYn(batchYn) ; 
			btVo.setMessage(message);
			return btVo ;
		}
		
		ready() ; 
		if ( is_wait ) {
			File jobfile = move_ready() ;
			insmap = insertTotalLedger(jobfile) ;
			ins_co = StringUtil.getLongValue(insmap, "ins_co") ;
		}
		message = StringUtil.getStringValue(insmap, "message") ;
		done(BTS_DONE, ins_co, message) ; 
		
		BatchVo btVo = new BatchVo(BDT021, batchYn) ;
		btVo.setExco(ins_co) ; 
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);

		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BDT021, ins_co, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		return btVo ; 
	}
	
	/** *********************************************************
	 * 건축물관리대장 > 총괄표제부 > 총괄표제부 등록
	 ** ********************************************************/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
        String batYn  = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batYn ) ;
		doExecute(batYn) ; 
	}
}
