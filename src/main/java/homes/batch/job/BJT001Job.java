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
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.PropertyUtil;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BJT001Job implements Job {
	
	public Logger Log = LogManager.getLogger(BJT001Job.class) ;

	private final BatchMapper mapper ; 
	
	public final String BTJOB_BASE_PATH = PropertyUtil.getPropVal("batch.job.base.path") ; 
	public final String BTJOB_DONE_PATH = PropertyUtil.getPropVal("batch.job.done.path") ; 

	public final String BJT001 = "BJT001" ;
	
	public String[] getHeader() {
		String[] headers = {
				"pkBuldRegstr", "regstrGbCd", "regstrGbNm", "regstrKndCd", "regstrKndNm", "nwOdGbCd", "nwOdGbNm", "plotLoc", "rdnmPlotLoc", "buldNm"
				, "sggCd", "legCd", "plogGbCd", "bun", "ji", "spPlotNm", "block", "lot", "outLotCo", "nwAddrRoadCd"
				, "nwAddrLegCd", "nwAddrGndCd", "nwAddrMstB", "nwAddrSubB", "plotAr", "buldAr", "buldLndRt", "totalAr", "bulkCalcTotAr", "bulkRt"
				, "mstPpsCd","mstPpsCdNm", "etcPps", "hshld1Co", "hshld2Co", "mstBuldCo", "subBuldCo", "subBuldAr", "totParkngCo", "inMechaCo"
				, "inMechaAr", "outMechaCo", "outMechaAr", "inIndpntCo", "inIndpntAr", "outIndpntCo", "outIndpntAr", "prmissDe", "stwkDe", "useConfDe"
				, "prmissNoYyyy", "prmissNoOrgCd", "prmissNoOrgNm", "prmissNoGbCd", "prmissNoGbNm", "hoCo", "energyEfcnyGrad", "energyRedcnRt", "energyEpiScr", "ecoBuldGrad"
				,  "ecoBuldScr", "brinBuldGrad", "brinBuldScr", "creatDe"
			} ; 
			return headers ; 
	}

	public int insertOneBJT001(String rowdata) {
		String[] cols  = this.getHeader() ;
		String[] datas = rowdata.split("[|]") ;
		
		CommonMap pMap = new CommonMap() ;
		for ( int i = 0; i < datas.length; i ++ ) {
			String column = cols[i] ; 
			String data   = datas[i] ;

			if ("outLotCo".equals(column) || "hshld1Co".equals(column) || "hshld2Co".equals(column) || "mstBuldCo".equals(column) 
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
		pMap.put("batchYn", "Y") ;
		return mapper.insertSummryTitlledgr(pMap) ;
	}
	
	
	public int deleteOneBJT001(String rowdata) {
		String[] headers =rowdata.split("[|]") ;
		String pkBld = headers[0] ;
		return mapper.deleteSummryTitlledgr(pkBld) ;
	}

	
	@Transactional
	public int doExecute() {
		CommonMap btmap = new CommonMap() ;
		FileReader filereader = null ;
		BufferedReader bufReader = null ; 
		int ins_co = 0 ; 
		btmap.put("uuid"   , UUID.randomUUID().toString()) ;
		btmap.put("batchty", BJT001) ;
		btmap.put("sttuscd", "BTS001" ) ;
		try {
			mapper.insertBatchjob(btmap) ;
			File dir = new File(BTJOB_BASE_PATH + "/" + BJT001) ;
			if ( dir.list().length > 0 ) {
				for ( String f_name : dir.list()) {
					File file = new File( dir.getPath() + File.separator + f_name ) ; 
					btmap.put("filenm", f_name) ;
					filereader = new FileReader(file) ;
					bufReader = new BufferedReader(filereader) ;
					String line = "" ;
					while((line = bufReader.readLine()) != null) {
						this.deleteOneBJT001(line) ;
						this.insertOneBJT001(line) ;
						ins_co++ ;
						Log.info(" *** exco             : {}", ins_co) ; 
					}
					bufReader.close() ;
					filereader.close()  ; 
					
					btmap.put("exco", ins_co) ;
					btmap.put("message", f_name + " 작업완료(" + ins_co + ")") ; 

					
					/* 작업끝난파일 이동 */  
					File doneDir  = new File(BTJOB_DONE_PATH +File.separator + BJT001) ;
					File srcfile  = new File(dir.getPath() + File.separator + f_name) ;
					File donefile = new File(BTJOB_DONE_PATH +File.separator + BJT001 +  File.separator + "done-" + System.currentTimeMillis() + "-" + f_name) ;
					doneDir.mkdirs() ; 
					Log.info(" *** batch done path  : {}", BTJOB_DONE_PATH) ; 
					Log.info(" *** batch source file: {}", srcfile.getAbsolutePath()) ;
					Log.info(" *** batch done   file: {}", donefile.getAbsolutePath()) ; 
					srcfile.renameTo(donefile) ; 
				}
			} else {
				Log.info("*** 작업대상 파일이 없습니다.") ; 
				btmap.put("filenm", "") ;
				btmap.put("exco", 0) ;
				btmap.put("message", "작업대상 파일이 없습니다.") ; 
			}
			btmap.put("sttuscd", "BTS002") ; 
		} catch ( FileNotFoundException e ) {
			btmap.put("sttuscd", "BTS999") ; 
			Log.error("*** Batch Error: FileNotFoundException: {}", e.getMessage()) ;
		} catch ( IOException e ) {
			btmap.put("sttuscd", "BTS999") ; 
			Log.error("*** Batch Error: IOException: {}", e.getMessage()) ;
		} finally {
			try { if ( bufReader != null ) bufReader.close() ; } catch ( IOException e ) { bufReader = null ; }
			try { if ( filereader != null ) filereader.close() ; } catch ( IOException e ) { filereader = null ; }
			mapper.updateBatchjob(btmap) ; 
		}
		return ins_co ;
	}

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Log.info("*** execute batchjob, jobid [ {} ] started at {}", BJT001, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
        int exco = this.doExecute() ;
		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BJT001, exco, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
	} 

}
