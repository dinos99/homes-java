package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
public class BJT000Job implements Job {
	
	public Logger Log = LogManager.getLogger(BJT000Job.class) ;

	private final BatchMapper mapper ; 

	public final String BTJOB_WAIT_PATH  = HomesProperty.getPropVal("batch.job.wait.path")  ; 
	public final String BTJOB_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path")  ; 
	public final String BTJOB_DONE_PATH  = HomesProperty.getPropVal("batch.job.done.path")  ; 
	
	public final String BJT000    = EnumBatchJob.BJT000.getCode() ; /* 건축물대장파일 분할작업 */ 
	public final String BJT001    = EnumBatchJob.BJT001.getCode() ; /* 단지정보입력 */ 
	public final String BJT002    = EnumBatchJob.BJT002.getCode() ; /* 단지정보입력 */ 
	public final String BLD005    = EnumBatchJob.BLD005.getCode() ;	/* 건축물대장-부속지번 */ 
	public final String BLD009    = EnumBatchJob.BLD009.getCode() ;	/* 건축물대장-전유부 */ 
	
	public final String BTS_PROC  = "BTS001" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_DONE  = "BTS002" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	
	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String ORIGIN_FILE_PREFIX = "ORIGIN-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 

	public String BTJOB_READY_JOB_PATH = "" ;
	public String BTJOB_WAIT_JOB_PATH  = "" ;
	
	public int SPLIT_LINE = 1000 ; 
	
	public int doSplit(String jobid, String uuid, File jbfile) {
		FileReader fr = null ;
		BufferedReader br = null ;
		
		FileWriter fw  = null ; 
		PrintWriter wr = null ;
		
		double fidx   = SPLIT_LINE ;
		int    spco   = 0 ; 
		int    lineno = 1 ; 
		/* SP-{jobid}-{uuid}-{fidx}.txt */
		Log.info("*** Batchfile split started ...") ;
		Log.info("*** source file: {}" , jbfile.getAbsolutePath()) ;
/*
		if ( BLD004.equals(jobid)) {
			SPLIT_LINE = SPLIT_LINE * 1000 ;
			fidx = SPLIT_LINE ;
			Log.info("*** SPLIT_LINE: {}", StringUtil.getCurrencyFormat(SPLIT_LINE));
		}
*/
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			while((line = br.readLine()) != null ) {
				String[] headers = line.split("[|]") ;
				String   gbcd    = headers[1] ;
				if ( "2".equals(gbcd)) { /* 데이터가 너무 많아서 현재는 집합건물만 먼저 */ 
					/* 전유부는 그래도 많다... 서울지역만 입력 */ 
					if ( BLD009.equals(jobid) && headers[8].indexOf("11") == 0 ) {
						lineno ++ ;
						String sp_filenm = SP_FILE_PREFIX + jobid + "-" + uuid + "-" + fidx + SP_FILE_EXTENTION ;
						File sp_file = new File(BTJOB_READY_JOB_PATH + File.separator + sp_filenm) ;
						if ( fw == null ) fw = new FileWriter(sp_file);
						if ( wr == null ) wr = new PrintWriter(fw);
						if ((lineno % SPLIT_LINE) == 1 ) {
							fidx ++ ; 
							spco ++ ; 
							
							if ( fw != null ) fw.close() ;
							if ( wr != null ) wr.close(); 
							fw = new FileWriter(sp_file);             
							wr = new PrintWriter(fw);
						}
						wr.println(line);
					} else if ( BLD005.equals(jobid)) {
						/* 부속건물도 집합건물만 입력 ( 일반건물은 PK중복 ) */ 
						spco ++ ;
						String sp_filenm = SP_FILE_PREFIX + jobid + "-" + uuid + "-" + SP_FILE_EXTENTION ;
						File sp_file = new File(BTJOB_READY_JOB_PATH + File.separator + sp_filenm) ;
						if ( fw == null ) fw = new FileWriter(sp_file);
						if ( wr == null ) wr = new PrintWriter(fw);
						wr.println(line);
					} else if ( !BLD009.equals(jobid)) {
						lineno ++ ;
						String sp_filenm = SP_FILE_PREFIX + jobid + "-" + uuid + "-" + fidx + SP_FILE_EXTENTION ;
						File sp_file = new File(BTJOB_READY_JOB_PATH + File.separator + sp_filenm) ;
						if ( fw == null ) fw = new FileWriter(sp_file);
						if ( wr == null ) wr = new PrintWriter(fw);
						if ((lineno % SPLIT_LINE) == 1 ) {
							fidx ++ ; 
							spco ++ ; 
							
							if ( fw != null ) fw.close() ;
							if ( wr != null ) wr.close(); 
							fw = new FileWriter(sp_file);             
							wr = new PrintWriter(fw);
						}
						wr.println(line);
					}
				} 
			}
			if ( wr != null ) wr.close();  
			if ( fw != null ) fw.close();  
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
			
			Log.error("*** wait path    : {}", BTJOB_READY_PATH) ;
			Log.error("*** wait job path: {}", BTJOB_READY_JOB_PATH) ;
			Log.error("*** jobid        : {}", jobid) ;             
			Log.error("*** split count  : {}", StringUtil.getCurrencyFormat(spco)) ;
			
			/* 작업결과물 이동 */ 
			/* 원본파일 => done 폴더로 이동 */ 
			File dir_done = new File(BTJOB_DONE_PATH) ; 
			if ( !dir_done.isDirectory()) {
				dir_done.mkdirs() ;
			}
			File dest = new File( BTJOB_DONE_PATH + File.separator + ORIGIN_FILE_PREFIX + jobid + "-" + uuid + SP_FILE_EXTENTION  ) ; 
			jbfile.renameTo(dest) ;
			Log.error("*** move origin file: {}", dest.getAbsolutePath()) ;

		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		return spco ; 
	}
	
	@Transactional
	public int doExecute() {
		/* 건축물대장-분할대기파일 업로드여부 확인 */
		int tot_spco = 0 ; 
		CommonMap btmap = new CommonMap() ;
		for ( EnumBatchJob enumcd: EnumBatchJob.values()) {
			if ( !BJT000.equals(enumcd.getCode()) 
				 && !BJT001.equals(enumcd.getCode())
				 && !BJT002.equals(enumcd.getCode())
				 && enumcd.getCode().indexOf("BTS") < 0 ) {
				String jobid = enumcd.getCode() ; 
//				File dir_ready = new File( BTJOB_READY_PATH + File.separator + jobid) ;
				BTJOB_WAIT_JOB_PATH  = BTJOB_WAIT_PATH  + File.separator + jobid ; 
				BTJOB_READY_JOB_PATH = BTJOB_READY_PATH + File.separator + jobid ;
				File dir_wait  = new File( BTJOB_WAIT_JOB_PATH) ;
				File dir_ready = new File( BTJOB_READY_JOB_PATH) ;
				if ( !dir_wait.isDirectory()) dir_wait.mkdirs() ;
				if ( !dir_ready.isDirectory()) dir_ready.mkdirs() ;
				
				/* 파일존재여부 확인 없으면 빠져나간다. */
				String[] fileList = dir_wait.list() ;
				if ( fileList == null || fileList.length == 0) {
					Log.info("*** File does not exist: {} directoy ", jobid) ;  
					continue ;
				}

				String bt_uuid = UUID.randomUUID().toString() ; 
				btmap.put("uuid"   , bt_uuid) ;
				btmap.put("batchty", BJT000) ;
				btmap.put("sttuscd", BTS_PROC ) ;
				mapper.insertBatchjob(btmap) ; 
				
				int spco = 0 ; 
				if ( fileList != null && fileList.length > 0) {
					for ( String fnm : fileList) {
						File btfile = new File ( BTJOB_WAIT_JOB_PATH + File.separator + fnm) ;
						spco = spco + doSplit(jobid, bt_uuid, btfile) ; 
					} 
				}
				tot_spco = tot_spco+ spco ; 
				String bt_message = "파일을 분할하였습니다. 작업건수(" + StringUtil.getCurrencyFormat(spco) + ")" ; 
				btmap.put("exco"   , spco) ; 
				btmap.put("sttuscd", BTS_DONE ) ;
				btmap.put("filenm" , SP_FILE_PREFIX + jobid + "-" + bt_uuid + SP_FILE_EXTENTION) ; 
				btmap.put("message", bt_message ) ;
				
				mapper.updateBatchjob(btmap) ;
			}
		}
		return tot_spco ;
	}
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		doExecute() ;
	}

}
