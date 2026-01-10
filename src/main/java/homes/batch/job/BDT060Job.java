package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BDT060Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT060Job.class) ;

	private final BatchMapper mapper ; 

	public final String BDT060 = EnumBatchJob.SPLIT_PSSION_AREA.getCode() ; 
	
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT060  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT060  ; 
	public final String BTJOB_DONE_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT060  ; 
	
	public final String FILE_PRIFIX = "ready-" ; 
	public final String FILE_EXTENTION = ".csv" ; 

	public final String BTS_PROC  = "BTS001" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_DONE  = "BTS002" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_ERROR = "BTS999" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 

	public final int ARCODE = 11710;
	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String SP_FILE_EXTENTION  = ".csv" ; 
	public final String WAIT_FILE_PREFIX     = "wait-" ; 
	public final String WAIT_FILE_EXTENTION  = ".csv" ; 
	public final String DONE_FILE_PREFIX     = "done-" ; 
	public final String DONE_FILE_EXTENTION  = ".csv" ; 
	

	public String bt_uuid = "" ; 
	public String batchYn = "" ; 
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		File done_dir  = new File( BTJOB_DONE_PATH ) ; 
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !done_dir.isDirectory()) done_dir.mkdirs() ; 
	}
	
	public String check_jobFile() {
		String message = "" ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ;
		if ( ready_dir.isDirectory() && ready_dir.list().length > 0 ) {
			if ( ready_dir.list().length > 1 ) {
				message = "작업대상 파일이 여러건 존재합니다." ; 
			} else {
				message = "EXISTS_JOB_FILE" ; 
			}
		} else {
			message = "작업대상 파일이 없습니다." ; 
		}
		return message ; 
	}
	
	public File get_jobfile() {
		Log.info("*** job file path: {}", BTJOB_READY_PATH + File.separator + FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		return new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + BDT060 + this.bt_uuid + FILE_EXTENTION ) ;
	}
	
	public void move_ready() {
		File ready_dir = new File( BTJOB_READY_PATH ) ;
		File f_ready   = new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + BDT060 + this.bt_uuid + FILE_EXTENTION ) ;
//		Log.info("*** is directory for wait_dir ? => {}", wait_dir.isDirectory());
//		Log.info("*** is exist file for wait_dir only one => {}", wait_dir.list().length);

		if ( ready_dir.isDirectory() && ready_dir.list().length == 1 ) {
			String f_wait_nm = ready_dir.list()[0] ; 
//			Log.info("*** is exist file for wait_dir only one => file name is {}", f_wait_nm);
			File f_wait = new File(ready_dir.getAbsolutePath() + File.separator + f_wait_nm ) ;
			f_wait.renameTo(f_ready) ;
		}
	}

	public void move_done() {
		/* ready folder 이동 */
		File wait_dir = new File( BTJOB_WAIT_PATH ) ;
		File f_wait   = new File( BTJOB_WAIT_PATH + File.separator + WAIT_FILE_PREFIX + BDT060 + "-" + this.bt_uuid + "-" + ARCODE + FILE_EXTENTION ) ;
		Log.error("**** move_done, target: {}", f_wait.getAbsolutePath()) ;
		if ( wait_dir.isDirectory() && wait_dir.list().length == 1 ) {
			Log.info("*** wait real file: {}", wait_dir.getAbsolutePath() + wait_dir.list()[0]) ; 
			Log.info("*** wait file: {}", f_wait.getAbsolutePath()) ; 
//			String f_done_nm = wait_dir.list()[0] ;
			String f_done_nm = DONE_FILE_PREFIX + BDT060 + "-" + this.bt_uuid + "-" + ARCODE ;
			f_done_nm += "-" + DateTimeUtil.getToday() + DONE_FILE_EXTENTION ;
			File f_done = new File(BTJOB_DONE_PATH + File.separator + f_done_nm ) ;
			f_wait.renameTo(f_done) ; 
		}
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
	
	public int doSplit() {
		int sp_no = 1 ; 
		File src_file = get_jobfile() ;

		FileReader fr = null ;
		BufferedReader br = null ;
		FileWriter fw = null ;
		PrintWriter wr = null ;
				
		try {
			fr = new FileReader(src_file) ;
			br = new BufferedReader(fr) ;

			String line = "" ;
			String wait_filenm = WAIT_FILE_PREFIX + BDT060 + "-" + this.bt_uuid + "-" + ARCODE + WAIT_FILE_EXTENTION ;
			File wait_file     = new File(BTJOB_WAIT_PATH + File.separator + wait_filenm) ;

			if ( fw == null ) fw = new FileWriter(wait_file);
			if ( wr == null ) wr = new PrintWriter(fw);
			
			int no = 1 ; 
			while((line = br.readLine()) != null ) {
				String[] datas = line.split("[|]") ;  
				String dt_arcode = datas[8] ; 
				String dt_mainAt = datas[28] ; 
				String[] ins_data = new String[12] ; 
				if ( "11710".equals(dt_arcode) && "0".equals(dt_mainAt)) {
					ins_data[0] = datas[0] ; // REGSTR_PK
					ins_data[1] = datas[7] ; // BULDNM
					ins_data[2] = datas[21] ; // DONGNM
					ins_data[3] = datas[22] ; // HOSILNM
					ins_data[4] = datas[23] ; // FLGBCD
					ins_data[5] = datas[25] ; // FLNO
					ins_data[6] = datas[26] ; // PUBLIC_AT
					ins_data[7] = datas[30] ; // FLNM
					ins_data[8] = datas[34] ; // PPSCD
					ins_data[9] = datas[36] ; // PPSETCNM
					ins_data[10] = datas[37] ; // TOTAL_AR
					ins_data[11] = datas[38] ; // CRDE
					String data = String.join("|", ins_data) ; 
					wr.println(data) ;
					Log.info("*** Line: {}", no) ; 
					no ++ ;
//					break ; 
				}
			}
			
			if ( wr != null ) wr.close();  
			if ( fw != null ) fw.close();  
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
			
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		
		return sp_no ; 
	}
	
	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready() {
		this.bt_uuid = UUID.randomUUID().toString() ;
		move_ready() ; 
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT060) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , FILE_PRIFIX + BDT060 + "-" + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", EnumBatchJob.SPLIT_PSSION_AREA.getName() + "을 시작합니다.") ;
		mapper.insertBatchjob(btmap) ;
	}

	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, long exco, String message) {
		move_done() ;
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , exco) ;
		btmap.put("filenm" , FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", message) ;
		mapper.updateBatchjob(btmap) ;
	}
	
	public BatchVo doExecute(String batYn) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = batYn ; 

		/* 기본폴더 생성 */ 
		create_jobdir() ;
		String message = EnumBatchJob.SPLIT_PSSION_AREA.getName() + " 시도별 파일을 분할합니다." ;
		message = check_jobFile() ;
		
		if ("EXISTS_JOB_FILE".equals(message)) {
			ready() ; 
			int sp_co = doSplit() ;
			message = EnumBatchJob.SPLIT_PSSION_AREA.getName() + "을 완료하였습니다.(" + sp_co + ")" ;
			done(BTS_DONE, sp_co, message) ; 
		} 

		BatchVo btVo = new BatchVo(BDT060, batchYn) ;
		btVo.setExco(0);
		btVo.setBatchYn(batchYn) ; 
		btVo.setMessage(message);
		
		return btVo ; 
	}
	
	
	public BatchVo doExecute(CommonMap params) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = "Y" ; 
		
		/* 기본폴더 생성 */ 
		create_jobdir() ;
		String message = EnumBatchJob.SPLIT_PSSION_AREA.getName() + " 시도별 파일을 분할합니다." ;
		message = check_jobFile() ;
		
		if ("EXISTS_JOB_FILE".equals(message)) {
			ready() ; 
			int sp_co = 0 ;
			sp_co = doSplit() ;
			message = EnumBatchJob.SPLIT_PSSION_AREA.getName() + "을 완료하였습니다.(" + sp_co + ")" ;
			done(BTS_DONE, sp_co, message) ; 
		} 
		
		BatchVo btVo = new BatchVo(BDT060, batchYn) ;
		btVo.setExco(0);
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
        String batYn  = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batYn ) ;
		doExecute(batYn) ; 
	}
}
