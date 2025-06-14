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
public class BLD002Job implements Job {
	
	public Logger Log = LogManager.getLogger(BLD002Job.class) ;

	private final BatchMapper mapper ; 

	public final String BLD002    = EnumBatchJob.BLD002.getCode() ;	
	
	public final String BTS_PROC  = EnumBatchJob.BTS001.getCode() ; 
	public final String BTS_DONE  = EnumBatchJob.BTS002.getCode() ;  
	public final String BTS_ERROR = EnumBatchJob.BTS999.getCode() ;  
	
	public final String BTJOB_BASE_PATH  = HomesProperty.getPropVal("batch.job.base.path")  + File.separator + BLD002 ; 
	public final String BTJOB_DONE_PATH  = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BLD002 ; 
	public final String BTJOB_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BLD002 ; 

	
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

	public int insertTotalTitleLedger(String rowdata, String batchYn) {
		String[] cols  = this.getHeader() ;
		String[] datas = rowdata.split("[|]") ;
		
		CommonMap pMap = new CommonMap() ;
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
		pMap.put("batchYn", batchYn) ;
		return mapper.insertTotalTitleLedgr(pMap) ;
	}
	
	
	public int deleteTotalTitleLedger(String rowdata) {
		String[] headers = rowdata.split("[|]") ;
		String pkBld = headers[0] ;
		return mapper.deleteTotalTitleLedgr(pkBld) ;
	}
	
	@Transactional
	public int doExecute(BatchVo batVo) {
		moveTargetFile() ;
		int exco = 0 ; 
		CommonMap btmap = new CommonMap() ;
		FileReader filereader = null ;
		BufferedReader bufReader = null ;
		/* 작업폴더에 파일이 존재하는경우 */ 
		File dir_base  = new File( BTJOB_BASE_PATH ) ; 
		if ( dir_base.isDirectory() && dir_base.list().length > 0 ) {
			String f_name = "" ; 
			for ( String f_nm : dir_base.list()) {
				f_name = f_nm ; 
				break ;
			}
			String job_uuid = UUID.randomUUID().toString() ; 
			btmap.put("batchty", BLD002) ;
			btmap.put("sttuscd", BTS_PROC ) ;
			btmap.put("exco"   , exco ) ;
			btmap.put("uuid"   , job_uuid) ;
			mapper.insertBatchjob(btmap) ;
			File file = new File( dir_base.getPath() + File.separator + f_name ) ;
			Log.info("*** batch file: {}" , file.getAbsolutePath()) ;
			try {
				filereader = new FileReader(file) ;
				bufReader = new BufferedReader(filereader) ;
				String line = "" ;
				while((line = bufReader.readLine()) != null) {
					this.deleteTotalTitleLedger(line) ; 
					this.insertTotalTitleLedger(line, batVo.getBatchYn()) ;
					exco ++ ;
					Log.error(" *** exco : {}, batch file: {}", exco, file.getName()) ; 
				}
				bufReader.close() ;
				filereader.close()  ; 
				
				btmap.put("exco", exco) ;
				btmap.put("filenm" , f_name) ;
				btmap.put("message", "총괄표제부 작업완료(" + StringUtil.getCurrencyFormat(exco) + ")") ; 
				btmap.put("sttuscd", BTS_DONE ) ;

				File done_file = new File(BTJOB_DONE_PATH +  File.separator + "done-" + file.getName()) ;

				Log.error(" *** batch done path  : {}", BTJOB_DONE_PATH) ; 
				Log.error(" *** batch source file: {}", file.getAbsolutePath()) ;
				Log.error(" *** batch done   file: {}", done_file.getAbsolutePath()) ; 
				file.renameTo(done_file) ; 
				
			} catch ( IOException e ) {
				btmap.put("sttuscd", BTS_ERROR) ; 
				btmap.put("filenm", "") ;
				btmap.put("exco", 0) ;
				btmap.put("message", "[IO_EXCEPTION] 파일 입출력에러") ;
				Log.error("*** Batch Error: IOException: [{}][{}]:{}", BLD002, job_uuid, e.getMessage()) ;
			} finally {
				try { if ( bufReader != null ) bufReader.close() ; } catch ( IOException e ) { bufReader = null ; }
				try { if ( filereader != null ) filereader.close() ; } catch ( IOException e ) { filereader = null ; };
			}
			
			btmap.put("sttuscd", BTS_DONE ) ;
			btmap.put("exco"   , exco ) ;
			btmap.put("filenm" , f_name) ;
			mapper.updateBatchjob(btmap) ;
		}
		return exco ; 
	}
	
	@Transactional
	public int doExecute1(BatchVo batVo) {
		CommonMap btmap = new CommonMap() ;
		FileReader filereader = null ;
		BufferedReader bufReader = null ; 
		int ins_co = 0 ; 
		String bt_uuid = UUID.randomUUID().toString() ; 
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("batchty", BLD002) ;
		btmap.put("sttuscd", BTS_PROC ) ;
		try {
			File dir = new File(BTJOB_BASE_PATH + File.separator + BLD002) ;
			Log.info("*** batch exec path: {}", BTJOB_BASE_PATH + File.separator + BLD002) ;
			if (!dir.isDirectory()) {
				dir.mkdirs() ; 
			}
			
			if ( dir.list().length > 0 ) {
				mapper.insertBatchjob(btmap) ;
				for ( String f_name : dir.list()) {
					File file = new File( dir.getPath() + File.separator + f_name ) ;
					Log.info("*** batch file: {}" , file.getAbsolutePath()) ;
					btmap.put("filenm", f_name) ;
					filereader = new FileReader(file) ;
					bufReader = new BufferedReader(filereader) ;
					String line = "" ;
					while((line = bufReader.readLine()) != null) {
						this.deleteTotalTitleLedger(line) ;
						this.insertTotalTitleLedger(line, batVo.getBatchYn()) ;
						ins_co ++ ;
						Log.error(" *** exco : {}, batch file: {}", StringUtil.getCurrencyFormat(ins_co), file.getName()) ; 
					}
					bufReader.close() ;
					filereader.close()  ; 
					
					btmap.put("exco", ins_co) ;
					btmap.put("message", f_name + " 작업완료(" + StringUtil.getCurrencyFormat(ins_co) + ")") ; 
					
					/* 작업끝난파일 이동 */  
					File doneDir  = new File(BTJOB_DONE_PATH +File.separator + BLD002) ;
					File srcfile  = new File(dir.getPath() + File.separator + f_name) ;
					File donefile = new File(BTJOB_DONE_PATH +File.separator + BLD002 +  File.separator + "done-" + System.currentTimeMillis() + "-" + f_name) ;
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
			Log.error("*** Batch Error: FileNotFoundException: [{}][{}]:{}", BLD002, bt_uuid, e.getMessage()) ;
		} catch ( IOException e ) {
			btmap.put("sttuscd", BTS_ERROR) ; 
			btmap.put("filenm", "") ;
			btmap.put("exco", 0) ;
			btmap.put("message", "[IO_EXCEPTION] 파일 입출력에러") ;
			Log.error("*** Batch Error: IOException: [{}][{}]:{}", BLD002, bt_uuid, e.getMessage()) ;
		} finally {
			try { if ( bufReader != null ) bufReader.close() ; } catch ( IOException e ) { bufReader = null ; }
			try { if ( filereader != null ) filereader.close() ; } catch ( IOException e ) { filereader = null ; }
			mapper.updateBatchjob(btmap) ; 
		}
		return ins_co ;
	}

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Log.info("*** execute batchjob, jobid [ {} ] started at {}", BLD002, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		BatchVo batVo = new BatchVo(BLD002, "Y") ; 
        int exco = doExecute(batVo) ;
		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BLD002, exco, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
	} 

}
