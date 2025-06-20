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
import homes.comm.util.HomesProperty;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BDT040Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT040Job.class) ;

	private final BatchMapper mapper ; 

	public final String BDT040 = EnumBatchJob.SPLIT_PSSION_LEDGER.getCode() ; 
	public final String BDT041 = EnumBatchJob.INSERT_PSSION_LEDGER.getCode() ; 
	
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT040  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT041  ; 
	
	public final String BTJOB_READY_SPLIT_PATH = BTJOB_READY_PATH + File.separator + "sdcode" ; 

	public final String FILE_PRIFIX    = "SP-" ; 
	
	public final String[] SURFIX_SDCODE = { 
			"-11000", "-26000", "-27000", "-28000", "-29000", 
			"-30000", "-31000", "-41000", "-51000", "-43000", 
			"-44000", "-52000", "-46000", "-47000", "-48000",
			"-50000"
	} ;  	
	
	public final String FILE_EXTENTION = ".txt" ; 

	public final String BTS_PROC  = "BTS001" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_DONE  = "BTS002" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_ERROR = "BTS999" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 

	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 
	
	public String bt_uuid = "" ; 
	public String batchYn = "" ; 
	public Long   mngrno  = 0l ; 
	
	public int get_sdidx(String arcode) {
		String arcd = Optional.ofNullable(arcode).orElse("00000").substring(0, 2) ;
		int sdidx = -1 ; 
		for ( int i = 0 ; i < SURFIX_SDCODE.length; i ++ ) {
			if ( arcd.equals(SURFIX_SDCODE[i].substring(1, 3))) {
				sdidx = i ; 
				break ; 
			}
		}
		
		return sdidx ;
	}
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		File split_dir = new File( BTJOB_READY_SPLIT_PATH ) ; 
		
		Log.info("*** wait_dir : {}", BTJOB_WAIT_PATH);
		Log.info("*** ready_dir: {}", BTJOB_READY_PATH);
		Log.info("*** split_dir: {}", BTJOB_READY_SPLIT_PATH);
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !split_dir.isDirectory()) split_dir.mkdirs() ; 
	}
	
	public String check_jobFile() {
		String message = "" ; 
		File wait_dir = new File( BTJOB_WAIT_PATH ) ;
		if ( wait_dir.isDirectory() && wait_dir.list().length > 0 ) {
			if ( wait_dir.list().length > 1 ) {
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
		Log.info("*** job file path: {}", BTJOB_READY_PATH + File.separator + FILE_PRIFIX + BDT040 + "-" + this.bt_uuid + FILE_EXTENTION) ;
		return new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + BDT040 + "-" + this.bt_uuid + FILE_EXTENTION ) ;
	}
	
	public void move_ready() {
		/* ready folder 이동 */ 
		File wait_dir = new File( BTJOB_WAIT_PATH ) ;
		File f_ready  = new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + BDT040 + "-" + this.bt_uuid + FILE_EXTENTION ) ;

		if ( wait_dir.isDirectory() && wait_dir.list().length == 1 ) {
			String f_wait_nm = wait_dir.list()[0] ; 
			File f_wait = new File(wait_dir.getAbsolutePath() + File.separator + f_wait_nm ) ;
			f_wait.renameTo(f_ready) ;
		}
	}
	
	public CommonMap parseLine(String rowdata) {
		CommonMap pMap = new CommonMap() ;
		String[] datas = rowdata.split("[|]") ;
		
		/* 필요한것들만 가져오자 */
		pMap.put("buldRegstrPk", datas[ 0]) ;
		pMap.put("regstrGbCd"  , datas[ 1]) ;
		pMap.put("regstrKdCd"  , datas[ 3]) ;
		pMap.put("arcd"        , datas[ 8]) ;
		pMap.put("dongNm"      , datas[21]) ;
		pMap.put("hoNm"        , datas[22]) ;
		pMap.put("floorGbCd"   , datas[23]) ;
		pMap.put("floorGbNm"   , datas[24]) ;
		pMap.put("floorNo"     , datas[25]) ;
		pMap.put("crde"        , datas[26]) ;
		
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
	
	public int doSplit() {
		int sp_no = 1 ; 
		File src_file = get_jobfile() ;

		FileReader     fr = null ;
		BufferedReader br = null ;
				
		try {
			fr = new FileReader(src_file) ;
			br = new BufferedReader(fr) ;
			String[] sp_filenm = new String[SURFIX_SDCODE.length] ;
			File  [] sp_file   = new File  [SURFIX_SDCODE.length] ; 
			FileWriter[]  fw   = new FileWriter [SURFIX_SDCODE.length] ;
			PrintWriter[] wr   = new PrintWriter[SURFIX_SDCODE.length] ;
			
			for ( int fi = 0; fi < SURFIX_SDCODE.length; fi ++ ) {
				String sdcode = SURFIX_SDCODE[fi] ;
				sp_filenm[fi] = SP_FILE_PREFIX + BDT041 + "-" + this.bt_uuid + sdcode + SP_FILE_EXTENTION ;
				sp_file[fi]   = new File(BTJOB_READY_SPLIT_PATH + File.separator + sp_filenm[fi]) ; 

				if ( fw[fi] == null ) fw[fi] = new FileWriter(sp_file[fi]);
				if ( wr[fi] == null ) wr[fi] = new PrintWriter(fw[fi]);
			}

			CommonMap pMap    = null ;			
			String    pLine   = "" ; 
			Long      pLineno = 1l ;
			while((pLine = br.readLine()) != null ) {
				pMap = parseLine(pLine) ;
				String arcd = Optional.ofNullable(String.valueOf(pMap.get("arcd"))).orElse("00000") ;
				int sdidx   = get_sdidx(arcd) ;
				if ( sdidx >= 0 ) {
					wr[sdidx].println(pLine) ;
				}				
				
				pLineno ++ ;
			}			

			for ( int fi = 0; fi < SURFIX_SDCODE.length; fi ++ ) {
				if ( wr[fi] != null ) wr[fi].close();  
				if ( fw[fi] != null ) fw[fi].close(); 
			}
			
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
		/* wait => ready로 이동 */ 
		move_ready() ; 
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", BDT040) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , FILE_PRIFIX + BDT041 + "-" + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", "[건축물 관리대장]표제부 파일분할을 시작합니다.") ;
		btmap.put("mngrno" , this.mngrno) ;
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
		btmap.put("mngrno" , this.mngrno) ;
		mapper.updateBatchjob(btmap) ;
	}
	
	public BatchVo doExecute(String batYn, Long mgrno) {
		this.bt_uuid = UUID.randomUUID().toString() ;
		this.batchYn = batYn ; 
		this.mngrno  = mgrno ; 
		
		int sp_co = 0 ; 
		
		/* 기본폴더 생성 */ 
		create_jobdir() ;
		String message = "[건축물관리대장]전유부 시도별분할" ;
		message = check_jobFile() ;
		
		if ("EXISTS_JOB_FILE".equals(message)) {
			ready() ; 
			sp_co += doSplit() ;
			message = "[건축물관리대장]전유부 파일을 시도별로 분할하였습니다.(" + sp_co + ")" ;
			done(BTS_DONE, sp_co, message) ; 
		}

		BatchVo btVo = new BatchVo(BDT040, batchYn) ;
		btVo.setExco(sp_co);
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
        String mngrno = Optional.ofNullable(String.valueOf(dataMap.get("userno"))).orElse("0");
        this.batchYn = batYn ;
        this.mngrno  = Long.parseLong(mngrno) ;
        Log.info("*** is batch: {}, mngrno: {}", batYn, Long.parseLong(mngrno)) ;
		doExecute(batYn, Long.parseLong(mngrno)) ; 
	}
}
