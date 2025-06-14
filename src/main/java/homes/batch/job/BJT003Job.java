package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.comm.constants.EnumBatchJob;
import homes.comm.constants.EnumError;
import homes.comm.util.HomesProperty;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BJT003Job implements Job {	
	public Logger Log = LogManager.getLogger(BJT003Job.class) ;

	private final BatchMapper mapper ; 

	public final String BJT003    = EnumBatchJob.BJT003.getCode() ;
	public final String BTS_PROC  = EnumBatchJob.BTS001.getCode() ; 
	public final String BTS_DONE  = EnumBatchJob.BTS002.getCode() ; 
	
	public final String BTJOB_WAIT_PATH  = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BJT003 ; 
	public final String BTJOB_DONE_PATH  = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BJT003 ; 
	public final String BTJOB_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BJT003 ; 

	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String ORIGIN_FILE_PREFIX = "ORIGIN-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 

	public String BTJOB_READY_SDCODE_PATH = BTJOB_READY_PATH ; 
	
	public String bt_uuid = "" ;
	public String arcode  = "" ; 
	public String sdcode  = "" ;
	public String arname  = "" ; 
		
	@Transactional
	public void start() {
		CommonMap btmap = new CommonMap() ; 
		this.bt_uuid  = UUID.randomUUID().toString() ;
		
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("exco"   , 0) ;
		btmap.put("batchty", BJT003) ;
		btmap.put("sttuscd", BTS_PROC ) ;
		btmap.put("message", "[" + this.arname + "]지역 분할작업을 시작하였습니다.") ; 
		mapper.insertBatchjob(btmap) ; 
	}
	
	@Transactional
	public void finished(int item_co) {
		CommonMap btmap = new CommonMap() ; 
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("exco"   , item_co) ;
		btmap.put("batchty", BJT003) ;
		btmap.put("sttuscd", BTS_DONE ) ; 
		btmap.put("message",  "[" + this.arname + "]지역을 분할하였습니다.(" + StringUtil.getCurrencyFormat(item_co) + ")건") ; 
		mapper.updateBatchjob(btmap) ; 
	}

	public void createJobDir() {
		File dir_wait  = new File( BTJOB_WAIT_PATH ) ; 
		File dir_done  = new File( BTJOB_DONE_PATH ) ; 
		File dir_ready = new File( BTJOB_READY_PATH ) ; 
		
		if ( !dir_wait.isDirectory())  dir_wait.mkdirs() ;
		if ( !dir_done.isDirectory())  dir_done.mkdirs() ;
		if ( !dir_ready.isDirectory()) dir_ready.mkdirs() ;
//		this.bt_uuid  = UUID.randomUUID().toString() ;
	}
	
	public void createSdcodeDir(String sdcode ) {
		BTJOB_READY_SDCODE_PATH = BTJOB_READY_PATH + File.separator + sdcode ; 
		File dir_arcode = new File( BTJOB_READY_SDCODE_PATH ) ; 
		if ( !dir_arcode.isDirectory())  dir_arcode.mkdirs() ;
	}

	public void move_done( String uuid, File jobfile ) {
		/* 작업결과물 이동 */ 
		/* 원본파일 => done 폴더로 이동 */ 
		File dir_done = new File(BTJOB_DONE_PATH) ; 
		if ( !dir_done.isDirectory()) {
			dir_done.mkdirs() ;
		}
		String bt_uuid = uuid ;
		/* For Test */ 
		File dest = new File( BTJOB_DONE_PATH + File.separator + ORIGIN_FILE_PREFIX + BJT003 + "-" + bt_uuid + "-" + this.sdcode + "000" + SP_FILE_EXTENTION  ) ; 
		jobfile.renameTo(dest) ;
		Log.error("*** wait path    : {}", BTJOB_READY_PATH) ;
		Log.error("*** wait job path: {}", BTJOB_WAIT_PATH) ;
		Log.error("*** jobid        : {}", BJT003) ;            
		Log.error("*** move origin file: {}", BTJOB_DONE_PATH) ;
		
	}
	

	public int doSplit(String jobid, String uuid, File jbfile) {
		FileReader fr = null ;
		BufferedReader br = null ;
		
		FileWriter fw  = null ; 
		PrintWriter wr = null ;

		int lineno = 0 ; 
		/* SP-{jobid}-{uuid}-{arcode}.txt */
		Log.info("*** Batchfile split started ...") ;
		Log.info("*** source file: {}" , jbfile.getAbsolutePath()) ;

		/* arcode folder 생성 */ 
		createSdcodeDir(this.sdcode + "000") ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			String sp_filenm = SP_FILE_PREFIX + jobid + "-" + uuid + "-" + this.arcode + SP_FILE_EXTENTION ;
			File sp_file     = new File(BTJOB_READY_SDCODE_PATH + File.separator + sp_filenm) ;

			if ( fw == null ) fw = new FileWriter(sp_file);
			if ( wr == null ) wr = new PrintWriter(fw);
			
			Long i = 0l ;
			Long l = 0l ; 
			while((line = br.readLine()) != null ) {
//				Log.error(line) ;
				String[] headers  = line.split("[|]") ;
				String   h_arcode = headers[9] ;
				if (h_arcode == null || "".equals(h_arcode) || h_arcode.length() != 5 ) continue ;
//				h_sdcode = h_arcode.substring(0, 2) ;
				
				i ++ ;
				l ++ ;
//				Log.error("*** {}/{}, {}[{}]: {}", i, lineno, this.arname, this.arcode, headers) ;
				if ( h_arcode.equals(this.arcode)) {
					lineno ++ ;
					wr.println(line) ; 
				}
				if ( l > 99999 ) {
					l = 0l ;
					Log.error("*** {}[{}]: line count  : {}/{}", this.arname, this.arcode, StringUtil.getCurrencyFormat(lineno), StringUtil.getCurrencyFormat(i)) ;
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
		return lineno ; 
	}
	

	public int doExecute() {
		int sp_co   = 0 ;
		int item_co = 0 ; 
		createJobDir() ;

		/* 파일존재여부 확인 없으면 빠져나간다. */
		File dir_wait  = new File( BTJOB_WAIT_PATH ) ; 
		String[] fileList = dir_wait.list() ;
		Log.info("fileList length({})", fileList.length) ;
		if ( fileList == null || fileList.length == 0) {
			Log.info("*** File does not exist: {} directoy ", BJT003) ;
			return sp_co ; 
		}
		
		for ( String f_nm : fileList ) {
			
			int idx_ext = f_nm.indexOf(".") ; 
			String f_uuid = f_nm.substring(10, 46) ; 
			String f_sdcd = f_nm.substring(idx_ext - 5, idx_ext - 3) ;
			List<CommonMap> sggList = mapper.selectSggcodeList(f_sdcd) ;
			File btfile = new File ( BTJOB_WAIT_PATH + File.separator + f_nm) ;
			for ( CommonMap sdmap : sggList ) {
				this.sdcode = Optional.ofNullable((String)sdmap.get("sdcode")).orElse("11") ;
				this.arcode = Optional.ofNullable((String)sdmap.get("arcode")).orElse("11000") ;
				this.arname = Optional.ofNullable((String)sdmap.get("arname")).orElse("서울특별시") ;
				Log.info("*** {}[{}] 작업시작", this.arname, this.arcode) ;
				start() ;
				sp_co ++ ;  
				item_co = doSplit(BJT003, f_uuid, btfile) ;
				finished(item_co) ;
			}
			move_done(f_uuid, btfile) ;
		}
		return sp_co ;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		doExecute() ;
	}

}
