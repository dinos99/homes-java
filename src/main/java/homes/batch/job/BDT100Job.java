package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import homes.comm.constants.EnumBatchJob;
import homes.comm.constants.EnumError;
import homes.comm.util.HomesProperty;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

/** ***************************************************************
 * 기본개요 파일분할작업 
 * ***************************************************************/
@Component
@RequiredArgsConstructor
public class BDT100Job implements Job {
	public Logger Log = LogManager.getLogger(BDT100Job.class) ;
//	private final BatchMapper mapper ; 
	
	public final String BDT000 = EnumBatchJob.SPLIT_BASE_SUMMRY.getCode() ; 
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT000  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT000  ; 
	public final String BTJOB_DONE_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT000  ; 
	
	public String get_today() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(today);
	}
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		File done_dir  = new File( BTJOB_DONE_PATH ) ; 
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
		if ( !done_dir.isDirectory()) done_dir.mkdirs() ; 
	}

	public String[] getHeader() {
		String[] headers = {
			"bregstrPk", "tregstrPk", "buldgb", "regstrkd"
			, "arcd", "legcd", "bunjib", "bunjij"
			, "rdcode", "rdlegcd", "underAt", "bdmainbun", "bdsubbun"
			, "crde"
		} ; 
		return headers ; 
	}

	public CommonMap parseLine(String rowdata) {
		CommonMap pMap = new CommonMap() ;

		String[] datas = rowdata.split("[|]") ;
		String hbdno     = "BS" ;
		String bregstrPk = datas[ 0] ; 
		String tregstrPk = datas[ 1] ; 
		String buldgb    = datas[ 2] ;
		String regstrkd  = datas[ 4] ;
		String arcd      = datas[ 9] ;
		String legcd     = datas[10] ; 
		String bunjib    = datas[12] ; 
		String bunjij    = datas[13] ; 
		String bdmainbun = StringUtil.strLpad(datas[21], 5, '0') ; 
		String bdsubbun  = StringUtil.strLpad(datas[22], 5, '0') ; 
		 
		hbdno = hbdno + StringUtil.strLpad(buldgb, 2, '0') ;
		hbdno = hbdno + StringUtil.strLpad(regstrkd, 2, '0') ;
		hbdno = hbdno + arcd + legcd + bunjib + bunjij ; /* Length 18 */
		
		String pLine = bregstrPk + "|" + tregstrPk + "|" + buldgb + "|" + regstrkd + "|" 
		             + arcd + "|" + legcd + "|" + bunjib + "|" + bunjij + "|"
				     + datas[18] + "|" + datas[19] + "|" + datas[20] + "|"
		             + bdmainbun + "|" + bdsubbun + "|" + datas[29] ;
				
		/* 필요한것들만 가져오자 */
		pMap.put("bregstrPk", bregstrPk) ;
		pMap.put("tregstrPk", tregstrPk) ;
		pMap.put("buldgb"   , buldgb) ;
		pMap.put("regstrkd" , regstrkd) ;
		pMap.put("arcd"     , arcd) ;
		pMap.put("legcd"    , legcd) ;
		pMap.put("bunjib"   , bunjib) ;
		pMap.put("bunjij"   , bunjij) ;
		pMap.put("rdcode"   , datas[18]) ;
		pMap.put("rdlegcd"  , datas[19]) ;
		pMap.put("underAt"  , datas[20]) ;
		pMap.put("bdmainbun", bdmainbun) ;
		pMap.put("bdsubbun" , bdsubbun) ;
		pMap.put("crde"     , datas[29]) ;

		pMap.put("hbdno"    , hbdno) ; 
		pMap.put("rawdata"  , pLine ) ; 
		
		return pMap ;
	}
	public File get_ready_file() {
		File dir_ready = new File(BTJOB_READY_PATH) ; 
		File f_ready = dir_ready.listFiles()[0] ;
		Log.info("*** job file path: {}", f_ready.getAbsolutePath()) ;
		return f_ready ; 
	}
	
	public void read_ready_file(CommonMap params) {
		File f_ready = get_ready_file() ;
		FileReader fr = null ;
		BufferedReader br = null ;
		FileWriter fw = null ;
		PrintWriter wr = null ;
		
		String arcd   = params.getStringValue("arcd") ; 
		String legcd  = params.getStringValue("legcd") ;  
		String arcode = arcd + legcd ; 
		try {
			
			fr = new FileReader(f_ready) ;
			br = new BufferedReader(fr) ;

			String line  = "" ;
			String f_done_nm = BDT000 + "-" + arcd + "-" + legcd + "-" + get_today() + "-001.txt" ;
			File   f_done    = new File(BTJOB_DONE_PATH + File.separator + f_done_nm) ;

			if ( fw == null ) fw = new FileWriter(f_done);
			if ( wr == null ) wr = new PrintWriter(fw);
			int rnum = 1 ;
			while((line = br.readLine()) != null ) {
				CommonMap pLine = parseLine(line) ; 
				/* 표제부만 사용함 */ 
				String regstrkd = pLine.getStringValue("regstrkd") ; 
				/* 지역코드 일치해야지만 사용 */ 
				String p_arcd  = pLine.getStringValue("arcd") ; 
				String p_legcd = pLine.getStringValue("legcd") ;
				String p_arcode = p_arcd + p_legcd ; 
				if ( "2".equals(regstrkd) /* 일반건축물 */ 
					|| "3".equals(regstrkd)) { /* 집합건물 표제부 */ 
					/* 지역코드 일치해야지만 사용 */
					if ( arcode.equals(p_arcode)) {
//						Log.info("Line: {}", line );
						String hbdno = pLine.getStringValue("hbdno")
						             + StringUtil.strLpad(String.valueOf(rnum), 9, '0') ; 
						String f_Line = hbdno + "|" + pLine.getStringValue("rawdata") ; 
						Log.info(f_Line) ; 
						wr.println(f_Line) ;
						rnum ++ ; 
					}
				} 
			}
			Log.info("*** job finihed: rows: {}", rnum );
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } finally {
        	if ( wr != null ) wr.close() ;  
			if ( fw != null ) try { fw.close() ;  } catch ( IOException e ) {} ;  
			if ( br != null ) try { br.close() ;  } catch ( IOException e ) {} ;  
			if ( fr != null ) try { fr.close() ;  } catch ( IOException e ) {} ;  
        }
	}

	public void doExecute(CommonMap params ) {
		create_jobdir() ;
		read_ready_file( params ) ;
		
	}
	
	/** *********************************************************
	 * 건축물관리대장 > 기본개요 > 지역코드별 파일분할작업 실행
	 ** ********************************************************/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
        String batchYn     = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batchYn ) ;
        CommonMap params = new CommonMap() ; 
        params.put("batchYn", "N") ; 
		doExecute(params) ; 
	}
}
