package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumBatchJob;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.HomesProperty;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BLD009Job implements Job {
	
	public Logger Log = LogManager.getLogger(BLD009Job.class) ;

	private final BatchMapper mapper ; 

	public final String BLD009    = EnumBatchJob.BLD009.getCode() ;	
	public final String BTS_PROC  = EnumBatchJob.BTS001.getCode() ; 
	public final String BTS_DONE  = EnumBatchJob.BTS002.getCode() ;  
	public final String BTS_ERROR = EnumBatchJob.BTS999.getCode() ;  
	
	public final String BTJOB_BASE_PATH  = HomesProperty.getPropVal("batch.job.base.path")  + File.separator + BLD009 ; 
	public final String BTJOB_DONE_PATH  = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BLD009 ; 
	public final String BTJOB_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BLD009 ; 

	public String BTJOT_TARGET_BASE_NAME = "" ; 

	public void createJobDir() {
		File dir_base  = new File( BTJOB_BASE_PATH ) ; 
		File dir_done  = new File( BTJOB_DONE_PATH ) ; 
		File dir_ready = new File( BTJOB_READY_PATH ) ; 
		
		if ( !dir_base.isDirectory()) dir_base.mkdirs() ;
		if ( !dir_done.isDirectory()) dir_done.mkdirs() ;
		if ( !dir_ready.isDirectory()) dir_ready.mkdirs() ;
	}

	@Transactional( readOnly = true )
	public int moveTargetFile() {
		createJobDir() ;
		int mv_co = 0 ; 
		File dir_ready = new File( BTJOB_READY_PATH ) ;
		for ( String f_nm : dir_ready.list()) {
			File src  = new File( BTJOB_READY_PATH + File.separator + f_nm ) ; 
			/* 해당경로에 파일이 존재하지 않는경우만 이동 */ 
			File dir_base  = new File( BTJOB_BASE_PATH ) ; 
			if ( dir_base.isDirectory() && dir_base.list().length == 0 ) {
				File dest = new File( BTJOB_BASE_PATH + File.separator + f_nm ) ;
				src.renameTo(dest) ; 
				Log.error("*** move to file from {} ", BTJOB_READY_PATH + File.separator + f_nm) ;
				Log.error("*** move to file to   {} ", BTJOB_BASE_PATH  + File.separator + f_nm) ;
				mv_co ++ ; 
			}
			return mv_co ; 
		}
		return 0 ; 
	}
	
	public String[] getHeader() {
		String[] headers = {
				  "buldRegstrPk", "regstrGbCd"   , "regstrGbNm" , "regstrKdCd"  , "regstrKdNm"
				  , "plotLoc"   , "rdnmPloLoc"   , "buldNm"     , "arcd"        , "legcd"
				  , "plogGbCd"  , "bun"          , "ji"         , "spPlotNm"    , "block"
				  , "lot"       , "nwAddrRoadCd" , "nwAddrLegCd", "nwAddrGrndCd", "nwAddrMainB"
				  , "nwAddrSubB", "blockNm"      , "hoNm"       , "floorGbCd"   , "floorGbNm"
				  , "floorNo"   , "creatDe"
		} ; 
		return headers ; 
	}

	public int insertPssionLedgr(String rowdata, String batchYn) {
		String[] cols  = this.getHeader() ;
		String[] datas = rowdata.split("[|]") ;
		
		CommonMap pMap = new CommonMap() ;
		for ( int i = 0; i < datas.length; i ++ ) {
			String column = cols[i] ; 
			String data   = datas[i] ;

			if ("nwAddrMainB".equals(column) || "nwAddrSubB".equals(column) || "floorNo".equals(column)) {
				int iVal = StringUtil.getIntValue(data) ;
				pMap.put(column, iVal) ;
			}else {
				pMap.put(column, data) ;
			} 
		}
		pMap.put("batchYn", batchYn) ;
		return mapper.insertPssionLedgr(pMap) ;
	}
	
	
	public int deletePssionLedgr(String rowdata) {
		String[] headers =rowdata.split("[|]") ;
		String pkBld = headers[0] ;
		return mapper.deletePssionLedgr(pkBld) ;
	}

	
	@Transactional
	public int doExecute(BatchVo batVo) {
		moveTargetFile() ;
		CommonMap btmap = new CommonMap() ;
		FileReader filereader = null ;
		BufferedReader bufReader = null ; 
		int ins_co = 0 ; 
		String bt_uuid = UUID.randomUUID().toString() ;
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("batchty", BLD009) ;
		btmap.put("sttuscd", BTS_PROC ) ;
		try {
			File dir = new File( BTJOB_BASE_PATH) ;
			Log.info("*** batch exec path: {}", BTJOB_BASE_PATH ) ;
			if (!dir.isDirectory()) {
				dir.mkdirs() ; 
			}
			
			if ( dir.list().length > 0 ) {
				mapper.insertBatchjob(btmap) ;
				for ( String f_name : dir.list()) {
					File file = new File( BTJOB_BASE_PATH + File.separator + f_name ) ;
					Log.info("*** batch file: {}" , file.getAbsolutePath()) ;
					btmap.put("filenm", f_name) ;
					filereader = new FileReader(file) ;
					bufReader  = new BufferedReader(filereader) ;
					String line = "" ;
					while((line = bufReader.readLine()) != null) {
						this.deletePssionLedgr(line) ;
						this.insertPssionLedgr(line, batVo.getBatchYn()) ;
						ins_co ++ ;
						Log.error(" *** exco : {}, batch file: {}", StringUtil.getCurrencyFormat(ins_co), file.getName()) ; 
					}
					bufReader.close() ;
					filereader.close()  ; 
					
					btmap.put("exco", ins_co) ;
					btmap.put("message", "전유부 등록작업 완료(" + StringUtil.getCurrencyFormat(ins_co) + ")") ; 
					
					/* 작업끝난파일 이동 */  
					File doneDir  = new File( BTJOB_DONE_PATH ) ;
					File srcfile  = new File( BTJOB_BASE_PATH + File.separator + f_name) ;
					File donefile = new File( BTJOB_DONE_PATH + File.separator + "done-" + file.getName()) ;
					doneDir.mkdirs() ; 
					Log.error(" *** batch done path  : {}", BTJOB_DONE_PATH) ; 
					Log.error(" *** batch source file: {}", srcfile.getAbsolutePath()) ;
					Log.error(" *** batch done   file: {}", donefile.getAbsolutePath()) ; 
					srcfile.renameTo(donefile) ; 
				}
			} else {
				Log.info("*** 작업대상 파일이 없습니다.") ; 
				btmap.put("filenm", "") ;
				btmap.put("exco", 0) ;
				btmap.put("message", "작업대상 파일이 없습니다.") ; 
			}
			btmap.put("sttuscd", BTS_DONE) ; 
		} catch ( FileNotFoundException e ) {
			btmap.put("sttuscd", BTS_ERROR) ; 
			btmap.put("filenm", "") ;
			btmap.put("exco", 0) ;
			btmap.put("message", "[FILE_NOT_FOUND] 파일을 찾을 수 없습니다.") ; 
			Log.error("*** Batch Error: FileNotFoundException:[{}][{}]:{}", BLD009, bt_uuid, e.getMessage()) ;
		} catch ( IOException e ) {
			btmap.put("sttuscd", BTS_ERROR) ; 
			btmap.put("filenm", "") ;
			btmap.put("exco", 0) ;
			btmap.put("message", "[IO_EXCEPTION] 파일 입출력에러") ;
			Log.error("*** Batch Error: IOException: [{}][{}]:{}", BLD009, bt_uuid, e.getMessage()) ;
		} finally {
			try { if ( bufReader != null ) bufReader.close() ; } catch ( IOException e ) { bufReader = null ; }
			try { if ( filereader != null ) filereader.close() ; } catch ( IOException e ) { filereader = null ; }
			mapper.updateBatchjob(btmap) ; 
		}
		return ins_co ;
	}

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Log.info("*** execute batchjob, jobid [ {} ] started at {}", BLD009, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		BatchVo batVo = new BatchVo(BLD009, "Y") ; 
        int exco = this.doExecute(batVo) ;
		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BLD009, exco, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
	} 

}
