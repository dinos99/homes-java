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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.batch.mapper.BatchTitleLedgrMapper;
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
public class BDT031Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT031Job.class) ;

	private final BatchMapper mapper ; 
	private final BatchTitleLedgrMapper titleMapper ; 
	
	public final String BDT030 = EnumBatchJob.SPLIT_TITLE_LEDGER.getCode() ; 
	public final String BDT031 = EnumBatchJob.INSERT_TITLE_LEDGER.getCode() ; 
	
	public final String BTJOB_WAIT_BDT031_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT031  ; 
	public final String BTJOB_READY_BDT031_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT031  ; 
	public final String BTJOB_DONE_BDT031_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT031  ; 

	public final String BTJOB_READY_SPLIT_PATH   = BTJOB_READY_BDT031_PATH  + File.separator + "split"; 
	
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
	public Long   mngrno  = 0l ; 

	public String getUUID() {
		this.bt_uuid = UUID.randomUUID().toString() ; 
		return this.bt_uuid ; 
	}
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_BDT031_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_BDT031_PATH ) ; 
		File done_dir  = new File( BTJOB_DONE_BDT031_PATH ) ; 

		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !done_dir.isDirectory()) done_dir.mkdirs() ; 
	}
	
	public boolean is_exists_file(String path) {
		File dir = new File( path ) ;
		return ( dir.isDirectory() && dir.list().length > 0 ) ;
	}
	
	public File get_jobfile() {
		File split_dir = new File(BTJOB_READY_SPLIT_PATH) ;
		if ( split_dir.isDirectory() && split_dir.list().length > 0) {
			return new File(split_dir.getAbsolutePath() + File.separator + split_dir.list()[0]) ; 
		} else {
			return null ; 
		}
	}
	
	public File move_ready() {
		File wait_dir  = new File(BTJOB_WAIT_BDT031_PATH) ; 
		File ready_dir = new File(BTJOB_READY_BDT031_PATH) ;
		String f_nm = FILE_READY_PREFIX + BDT031  + "-" + this.bt_uuid + FILE_EXTENTION ; 
		File dest   = new File( ready_dir.getAbsolutePath() + File.separator + f_nm) ;
		if ( wait_dir.isDirectory() && wait_dir.list().length > 0 ) {
//			Log.info("wait  directory path: {}", wait_dir.getAbsolutePath()) ; 
//			Log.info("ready directory path: {}", ready_dir.getAbsolutePath()) ; 
			File source = new File( BTJOB_READY_SPLIT_PATH  + File.separator + wait_dir.list()[0] ) ;
			source.renameTo(dest) ;
		}
		return dest ; 
	}
	public void move_done(File source) {
//		String f_nm = FILE_DONE_PREFIX + BDT031 + "-" + this.bt_uuid + FILE_EXTENTION ;
		String f_nm = source.getName() ;
		File dest   = new File( BTJOB_DONE_BDT031_PATH + File.separator + f_nm) ; 
		source.renameTo(dest) ;
		Log.info("File move done path: {}", dest.getAbsolutePath()) ;
	}
	
	public String[] getHeader() {
		String[] headers = {
			"buldRegstrPk"       , "regstrGbCd"   , "regstrGbNm"  , "regstrKdCd"   , "regstrKdNm"   , "plotLoc"       , "rdnmPloLoc"   , "buldNm"         , "arcd"           , "legcd", 
			"plotGbCd"           , "bun"         , "ji"           , "spPlotNm"     , "block"          , "lot"           , "outLotCo"     , "nwAddrOadCd"    , "nwAddrEgCd"     , "nwAddrNdCd", 
			"nwAddrStB"          , "nwAddrUbB"   , "blockNm"      , "mainSubGbCd"  , "mainSubGbNm"    , "plotAr"        , "buldAr"       , "buldLndRt"      , "totalAr"        , "bulkCalcTotAr", 
			"bulkRt"             , "strctCd"     , "strctCdNm"    , "etcStrct"     , "mainPpsCd"      , "mainPpsCdNm"   , "etcPps"       , "rfCd"           , "rfCdNm"         , "etcRf",
			"hshldCo"            , "fmlyCo"      , "height"       , "grndFloorCo"  , "undrGrndFloorCo", "rdngElvtrCo"   , "emgncElvtrCo" , "subBuldCo"      , "subBuldAr"      , "totBlockAr", 
			"inMechaCo"          , "inMechaAr"   , "outMechaCo"   , "outMechaAr"   , "inIndpntCo"     , "inIndpntAr"    , "outIndpntCo"  , "outIndpntAr"    , "prmissDe"       , "stwkDe", 
			"occupancyApprovalDe", "prmissNoYyyy", "prmissNoOrgCd", "prmissNoOrgNm", "prmissNoGbCd"   , "prmissNoGbNm"  , "unitCo"       , "energyEfcnyGrad", "energyRedcnRt"  , "energyEpiScr", 
			"ecoBuldGrad"        , "ecoBuldScr"  , "brinBuldGrad" , "brinBuldScr"  , "creatDe"        , "erdsgnApplcYn" , "rserthqkAblty"
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

			if ("outLotCo".equals(column) || "nwAddrStB".equals(column) || "nwAddrUbB".equals(column) || "hshldCo".equals(column) || "fmlyCo".equals(column)
				|| "grndFloorCo".equals(column) || "undrGrndFloorCo".equals(column) || "rdngElvtrCo".equals(column) || "emgncElvtrCo".equals(column)
				|| "subBuldCo".equals(column) || "inMechaCo".equals(column) || "outMechaCo".equals(column) || "inIndpntCo".equals(column)
				|| "outIndpntCo".equals(column) || "unitCo".equals(column) || "fstUploadUserNo".equals(column)|| "lstChngUserNo".equals(column)
				|| "createUserNo".equals(column)|| "updtUserNo".equals(column)) {
				int iVal = StringUtil.getIntValue(data) ;
				pMap.put(column, iVal) ;
			} else if ( "plotAr".equals(column) || "buldAr".equals(column) || "buldLndRt".equals(column) || "totalAr".equals(column) || "bulkCalcTotAr".equals(column)
						|| "bulkRt".equals(column) || "height".equals(column) || "subBuldAr".equals(column) || "totBlockAr".equals(column) || "inMechaAr".equals(column)
						|| "outMechaAr".equals(column) || "inIndpntAr".equals(column) || "outIndpntAr".equals(column) || "energyRedcnRt".equals(column)
						|| "energyEpiScr".equals(column) || "ecoBuldScr".equals(column) || "brinBuldScr".equals(column)) {
				try {
					float fVal = StringUtil.getFloatValue(data) ;
					pMap.put(column, fVal) ;
				} catch ( NumberFormatException nfe ) {
					Log.error(nfe.getMessage()) ;
					Log.error("*** NumberFormatException: {} / {}", column , data);
				}
			} else {
				pMap.put(column, data) ;
			} 
		}
		pMap.put("batchYn", this.batchYn) ;
		pMap.put("mngrno" , this.mngrno) ; 
		return pMap ;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> insertTitleLedger(File jbfile) {
		Long ins_Ledgr = 0l ;
		Long ins_ablty = 0l ; 
		Long ins_ar    = 0l ;
		Long ins_pps   = 0l ;
		Long ins_de    = 0l ;
		Long ins_co    = 0l ; 
		
		Map<String, Object> insmap = new HashMap<String, Object>() ;
		
		FileReader fr = null ;
		BufferedReader br = null ;
		int no = 1 ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			while((line = br.readLine()) != null ) {
				CommonMap pMap = parseLine(line) ;
				
				try {
					ins_Ledgr += titleMapper.insertBdTitleLedgr(pMap) ;
					ins_ablty += titleMapper.insertBdTitleLedgrAblty(pMap) ;
					ins_ar    += titleMapper.insertBdTitleLedgrAr(pMap) ;
					ins_pps   += titleMapper.insertBdTitleLedgrpps(pMap) ;
					ins_de    += titleMapper.insertBdTitleLedgrde(pMap) ;
					ins_co    += titleMapper.insertBdTitleLedgrco(pMap) ;
				} catch ( DuplicateKeyException due) {
					/* 여기에 올리면 혹시 중복파일이 아닌지 의심해봐야 한다. */ 
					Log.error("*** DuplicateKeyException: Duplicated entry key {}", pMap.get("buldRegstrPk"));
					continue ; 
				} catch ( DataAccessException dae) {
					/* ************************************************************************************
					 * SPLIT LENGTH가 틀려지거나 입력값 사이즈가 컬럼 사이즈를
					 * 넘어섰을때 발생함( 컬럼사이즈 조정필요 )
					 * 특히 Float을 조심할것 (Ex: Column size FLOAT(5,2) => actual value (68321.0)
					 * Exception Log를 따로 뽑아서 나중에 처리하도록 한다.  
					 * ************************************************************************************/
					Log.error("*** DataAccessException: raw data: {}", line);
					continue ; 
				}
				if ((no % SPLIT_LINE) == 0) {
					Log.info("file {}, execute count: {}", jbfile.getName(), StringUtil.getCurrencyFormat(no)) ;
					Log.error("***************************************************") ;
		        	Log.error("*** inserted total Ledger    : {}", StringUtil.getCurrencyFormat(ins_Ledgr));
		        	Log.error("*** inserted total Ablty     : {}", StringUtil.getCurrencyFormat(ins_ablty));
		        	Log.error("*** inserted total Area      : {}", StringUtil.getCurrencyFormat(ins_ar));
		        	Log.error("*** inserted total pps       : {}", StringUtil.getCurrencyFormat(ins_pps));
		        	Log.error("*** inserted total prmiss de : {}", StringUtil.getCurrencyFormat(ins_de));
		        	Log.error("*** inserted total unit co   : {}", StringUtil.getCurrencyFormat(ins_co));
					Log.error("***************************************************") ;
				}
				no ++ ; 
			}
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close() ;
        	Log.error("*** job {} finished at: {} ", jbfile.getName(), DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
        	move_done(jbfile) ; 
		} catch ( IOException e ) {
        	Log.error("*** Batchfile IOException Error: file: {}:", BTJOB_DONE_BDT031_PATH + File.separator + jbfile.getName()) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		insmap.put("ins_co" , ins_Ledgr) ; 
		insmap.put("message", "[건축물관리대장]표제부 등록(" + StringUtil.getCurrencyFormat(ins_Ledgr) + "건)") ;
		return insmap ; 
	}
		
	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready() {
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT031) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", "[건축물 관리대장]표제부 데이터를 생성합니다.") ;
		btmap.put("mngrno" , this.mngrno) ;
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
		btmap.put("mngrno" , this.mngrno) ;
		mapper.updateBatchjob(btmap) ;
	}
	
	public BatchVo getBatchVo(String jobid, Long exco, String batchYn, String message) {
		BatchVo batVo = new BatchVo(jobid) ;
		batVo.setExco(exco) ; 
		batVo.setBatchYn(batchYn) ; 
		batVo.setMessage(message);
		batVo.setMngrno(this.mngrno);
		return batVo ;
	}
	
	public BatchVo doExecute(String batYn, Long mno) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = batYn ; 
		this.mngrno  = mno ;
		
		Map<String, Object> insmap = null ; 
		
		create_jobdir() ;
		Long ins_co = 0l ; 
		String message = "" ;
		
		boolean is_wait = is_exists_file(BTJOB_READY_SPLIT_PATH) ;
		
		if ( !is_wait) {
			done(BTS_DONE, 0, message) ;  
			/* [대기]디렉토리에 작업파일이 존재하지 않습니다. */
			return getBatchVo(BDT031, 0l, batYn, EnumBatchJob.MSG_NOT_EXIST_JOB_FILE_READY.getName()) ;
		}
		
		ready() ; 
		if ( is_wait ) {
			File jobfile = get_jobfile() ;
			if ( jobfile != null && jobfile.isFile()) {
				while( jobfile != null ) {
					insmap = insertTitleLedger(jobfile) ;
					ins_co += StringUtil.getLongValue(insmap, "ins_co") ;
					jobfile = get_jobfile() ;
				}
			} else {
				done(BTS_DONE, 0, message) ; 
				/* [대기]디렉토리에 작업파일이 존재하지 않습니다. */
				return getBatchVo(BDT031, 0l, batYn, EnumBatchJob.MSG_NOT_EXIST_JOB_FILE_READY.getName()) ;
			}
		}
		message = StringUtil.getStringValue(insmap, "message") ;
		done(BTS_DONE, ins_co, message) ; 
		
		BatchVo btVo = new BatchVo(BDT031, batchYn) ;
		btVo.setExco(ins_co) ; 
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);

		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BDT031, ins_co, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		return btVo ; 
	}
	
	/** *********************************************************
	 * 건축물관리대장 > 총괄표제부 > 총괄표제부 등록
	 ** ********************************************************/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
        String batYn  = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        String mngrno = Optional.ofNullable(String.valueOf(dataMap.get("userno"))).orElse("0");
        this.batchYn = batYn ;
        this.mngrno  = Long.parseLong(mngrno) ;
        Log.info("*** is batch: {}, mngrno: {}", batYn, Long.parseLong(mngrno)) ;
		doExecute(batYn, Long.parseLong(mngrno)) ; 
	}
}
