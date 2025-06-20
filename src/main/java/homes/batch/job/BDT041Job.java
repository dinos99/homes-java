package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
public class BDT041Job implements Job {
	public Logger Log = LogManager.getLogger(BDT041Job.class) ;

	private final BatchMapper mapper ; 
	
	public String bt_uuid = "" ; 
	public String batchYn = "" ; 
	public Long   mngrno  = 0l ; 

	public final String BDT041 = EnumBatchJob.INSERT_PSSION_LEDGER.getCode() ;
	
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT041  ; 
	public final String BTJOB_DONE_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT041  ; 
	public final String BTJOB_READY_SPLIT_PATH = BTJOB_READY_PATH + File.separator + "sdcode" ; 

	public final String BTS_PROC  = EnumBatchJob.BTS_PROC.getCode() ;  
	public final String BTS_DONE  = EnumBatchJob.BTS_DONE.getCode() ;
	public final String BTS_ERROR = EnumBatchJob.BTS_ERROR.getCode() ; 

	public final int SPLIT_LINE = 1000 ;
	
	public void create_jobdir() { 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ; 
		
		Log.info("*** ready_dir: {}", BTJOB_READY_PATH);
		Log.info("*** split_dir: {}", BTJOB_READY_SPLIT_PATH);
		 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !split_dir.isDirectory()) split_dir.mkdirs() ; 
	}
	
	public boolean check_workdir() {
		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ;
		
		int f_count = 0 ;
		for ( String f_nm : split_dir.list()) {
//			Log.info(split_dir.getAbsolutePath());
			File f = new File(split_dir.getAbsoluteFile() + File.separator + f_nm ) ;
//			Log.info(f.getName()) ; 
			if (f.isFile()) { 
				f_count ++ ;
			}
		}
		return f_count > 0 ; 
	}

	public File get_jobfile() {
		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ;
		File job_file  = null ; 

		for ( String f_nm : split_dir.list()) {
			File f = new File(split_dir.getAbsoluteFile() + File.separator + f_nm ) ;
			if (f.isFile()) { 
				job_file = new File(split_dir.getAbsoluteFile() + File.separator + f_nm ) ; 
			}
		} 
		
		return job_file ; 
	}

	public void move_done(File source) {
//		String f_nm = FILE_DONE_PREFIX + BDT031 + "-" + this.bt_uuid + FILE_EXTENTION ;
		String f_nm = source.getName() ;
		File dest   = new File( BTJOB_DONE_PATH + File.separator + f_nm) ; 
		source.renameTo(dest) ;
		Log.info("File move done path: {}", dest.getAbsolutePath()) ;
	}
	
	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready(String f_nm) {
		this.bt_uuid = UUID.randomUUID().toString() ;

		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT041) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , f_nm) ;
		btmap.put("message", "[건축물 관리대장]전유부 파일분할을 시작합니다.") ;
		btmap.put("mngrno" , this.mngrno) ;
		mapper.insertBatchjob(btmap) ;
	}

	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, long exco, String message, String f_nm) {
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , exco) ;
		btmap.put("filenm" , f_nm) ;
		btmap.put("message", message) ;
		btmap.put("mngrno" , this.mngrno) ;
		mapper.updateBatchjob(btmap) ;
	}

	public CommonMap parseLine(String rowdata) {
		CommonMap pMap = new CommonMap() ;
		String[] datas = rowdata.split("[|]") ;
		
		/* 필요한것들만 가져오자 */
		pMap.put("buldRegstrPk", datas[ 0]) ;
//		pMap.put("regstrGbCd"  , datas[ 1]) ;
//		pMap.put("regstrKdCd"  , datas[ 3]) ;
		pMap.put("arcd"        , datas[ 8]) ;
		pMap.put("dongNm"      , datas[21]) ;
		pMap.put("hoNm"        , datas[22]) ;
		pMap.put("floorGbCd"   , datas[23]) ;
		pMap.put("floorGbNm"   , datas[24]) ;
		pMap.put("floorNo"     , datas[25]) ;
		pMap.put("crde"        , datas[26]) ;
		
		pMap.put("mngrno"      , this.mngrno) ;
		
		/*
		Log.info("*** rowdata: {}", rowdata) ;
		Log.info("*****************************************************************") ;
		Log.info("*** arcd      : {}", pMap.get("arcd")) ;
		Log.info("*** dongNm    : {}", pMap.get("dongNm")) ;
		Log.info("*** hoNm      : {}", pMap.get("hoNm")) ;
		Log.info("*** floorGbCd : {}", pMap.get("floorGbCd")) ;
		Log.info("*** floorGbNm : {}", pMap.get("floorGbNm")) ;
		Log.info("*** floorNo   : {}", pMap.get("floorNo")) ;
		Log.info("*** crde      : {}", pMap.get("crde")) ;
		Log.info("*****************************************************************") ;
		*/
		return pMap ;
	}
	
	@Transactional
	public CommonMap updatePssionLedger( File source ) {
		CommonMap cmap = new CommonMap() ;
		
		FileReader     fr = null ;
		BufferedReader br = null ;
		
		int succ_co = 0 ;
		int fail_co = 0 ; 
		
		try {
			fr = new FileReader(source) ;
			br = new BufferedReader(fr) ;
			
			CommonMap pMap    = null ;			
			String    pLine   = "" ; 
			Long      pLineno = 1l ;
			while((pLine = br.readLine()) != null ) {
				pMap = parseLine(pLine) ;
				
				int up_co = mapper.updatePssionLedger(pMap) ;
				
				if ( up_co > 0 ) succ_co ++ ;
				else fail_co ++ ;
				
				if ((pLineno % SPLIT_LINE) == 0) {
					Log.info("Read {} Line, success: {}, failure: {}"
							,  StringUtil.getCurrencyFormat(pLineno)
							,  StringUtil.getCurrencyFormat(succ_co)
							,  StringUtil.getCurrencyFormat(fail_co)) ; 
				}
				if ( up_co <= 0 ) {
					Log.error("*** can not find entry key for {}", pMap.get("buldRegstrPk")) ;
				}
				pLineno ++ ;
			}

			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
			
        	Log.error("*** job {} finished at: {} ", source.getName(), DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
        	move_done(source) ; 
			
        	
        	cmap.put("exco"   , pLineno) ;
        	cmap.put("message", "[건축물 파일대장]전유부파일 갱신, 전체: " 
        	                    + StringUtil.getCurrencyFormat(pLineno) + "건, 성공: "
        	                    + StringUtil.getCurrencyFormat(succ_co) + "건, 실패: "
			                    + StringUtil.getCurrencyFormat(fail_co) + "건") 
        	;
        	
		} catch ( IOException e ) {
        	Log.error("*** pssionLedger file Error: {}:", e.getMessage()) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		
		return cmap ; 
	}
	
	public BatchVo doExecute(String batYn, Long mgrno) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = batYn ; 
		this.mngrno  = mgrno ; 
		create_jobdir() ;

		double ex_co = 0 ; 
		String message = EnumBatchJob.MSG_NOT_EXIST_JOB_FILE_READY.getName() ; 
		boolean is_jobfile = check_workdir() ;
		
		if ( is_jobfile) {
			File job_file = get_jobfile() ;
			ready(job_file.getName()) ;
			
			CommonMap cmap = updatePssionLedger( job_file ) ;
			
			ex_co   = cmap.getLongValue("exco") ; 
			message = cmap.getStringValue("message") ;
			done(BTS_DONE, cmap.getLongValue("exco"), message, job_file.getName()) ;
		} 
		
		BatchVo btVo = new BatchVo(BDT041, batYn) ;
		btVo.setExco(ex_co);
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);
		
		return btVo ; 
	}
	
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
