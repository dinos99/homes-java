package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.api.buld.vo.TitleLedgrVo;
import homes.batch.mapper.BatchMapper;
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
public class BDT300Job implements Job {
	public Logger Log = LogManager.getLogger(BDT300Job.class) ;
	private final BatchMapper mapper ; 
	
	public final String BDT300 = EnumBatchJob.SPLIT_TITLE_LEDGER.getCode() ; 
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT300  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT300  ; 
	public final String BTJOB_DONE_PATH   = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BDT300  ; 
	
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
	
	public TitleLedgrVo parseLine(int bdseq, String rowdata) {
		TitleLedgrVo vo = new TitleLedgrVo() ;
		
		String[] datas = rowdata.split("[|]") ;
		String buldno    = "BD" ;
		String bregstrPk = datas[ 0] ; 
		String buldgb    = datas[ 1] ; 

		String regstrkd  = datas[ 3] ;
		String bdaddr    = datas[ 5] ;
		String rdaddr    = datas[ 6] ; 
		String buldnm    = datas[ 7] ; 
		String arcd      = datas[ 8] ;
		String legcd     = datas[ 9] ; 
		String platgb    = datas[10] ; 
		String bunjib    = datas[11] ; 
		String bunjij    = datas[12] ; 
		String rdcode    = datas[17] ; 
		String rdlegcd   = datas[18] ; 
		String underAt   = datas[19] ; 
		
		String rdmainbun = StringUtil.strLpad(datas[20], 5, '0') ; 
		String rdsubbun  = StringUtil.strLpad(datas[21], 5, '0') ; 
		
		String dongnm    = datas[22] ; 
		String platAr    = Optional.ofNullable(datas[25]).orElse("0.0") ; 
		String buldAr    = Optional.ofNullable(datas[26]).orElse("0.0") ; 
		String totalAr   = Optional.ofNullable(datas[28]).orElse("0.0") ;
		String ppscd     = datas[34] ; 
		String ppsetcnm  = datas[36] ; 
		String s_hshldco = Optional.ofNullable(datas[40]).orElse("0") ; 
		String s_fmlyco  = Optional.ofNullable(datas[41]).orElse("0") ; 
		String s_grndco  = Optional.ofNullable(datas[43]).orElse("0") ; 
		String s_underco = Optional.ofNullable(datas[44]).orElse("0") ; 
		String sr_evltco = Optional.ofNullable(datas[45]).orElse("0") ; 
		String se_evltco = Optional.ofNullable(datas[46]).orElse("0") ; 
		String confde    = datas[60] ; 
		String s_roomco  = datas[66] ; 
		String crde      = datas[74] ;
		
		
		int rideEvltco = Integer.parseInt(sr_evltco) ; 
		int emgrEvltco = Integer.parseInt(se_evltco) ; 
		int n_groundco = Integer.parseInt(s_grndco) ;
		int n_underco  = Integer.parseInt(s_underco) ;
		int n_hshldco  = Integer.parseInt(s_hshldco) ; 
		int n_fmlyco   = Integer.parseInt(s_fmlyco) ; 
		int n_roomco   = Integer.parseInt(s_roomco) ; 
		
		Float f_totalAr = Float.parseFloat(totalAr) ; 
		Float f_buldAr  = Float.parseFloat(buldAr) ; 
		Float f_platAr  = Float.parseFloat(platAr) ;
		 
		buldno = buldno + StringUtil.strLpad(buldgb, 2, '0') ;
		buldno = buldno + StringUtil.strLpad(regstrkd, 2, '0') ;
		buldno = buldno + "0" + arcd + legcd  ; /* Length 17 */
		buldno = buldno + StringUtil.strLpad(String.valueOf(bdseq), 16, '0') ; 
				
		/* 필요한것들만 가져오자 */
		vo.setBuldno(buldno);
		vo.setBregstrPk(bregstrPk);
		vo.setBuldgb(buldgb);
		vo.setArcd(arcd);
		vo.setLegcd(legcd);
		vo.setBunjib(bunjib);
		vo.setBunjij(bunjij);
		vo.setBuldnm(buldnm);
		vo.setDongnm(dongnm);
		vo.setBdaddr(bdaddr);
		vo.setRdaddr(rdaddr) ; 
		vo.setPpscd(ppscd);
		vo.setPpsetcnm(ppsetcnm);
		vo.setCfmvgb("1"); 
		vo.setConfde(confde);
		vo.setMoveinde("");
		vo.setFlgroundco(n_groundco);
		vo.setFlunderco(n_underco);
		vo.setRideElvtrco(rideEvltco);
		vo.setEmgrElvtrco(emgrEvltco);
		vo.setHshldco(n_hshldco);
		vo.setFmlyco(n_fmlyco);
		vo.setRoomco(n_roomco);
		vo.setPlatgb(platgb) ;
		vo.setRdcode(rdcode);
		vo.setRdlegcd(rdlegcd);
		vo.setUnderAt(underAt);
		vo.setRdMainBun(rdmainbun);
		vo.setRdSubBun(rdsubbun);
		vo.setTotalAr(f_totalAr);
		vo.setSupplyAr(0.0f);
		vo.setBuldAr(f_buldAr);
		vo.setPlatAr(f_platAr);
		vo.setCrde(crde); 
		
		vo.setUseYn("Y");
		vo.setBatchYn("Y");
		
		return vo ;
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
			String f_done_nm = BDT300 + "-" + arcd + "-" + legcd + "-" + get_today() + ".txt" ;
			File   f_done    = new File(BTJOB_DONE_PATH + File.separator + f_done_nm) ;

			if ( fw == null ) fw = new FileWriter(f_done);
			if ( wr == null ) wr = new PrintWriter(fw);
			int rnum = 1 ;
			while((line = br.readLine()) != null ) {
				TitleLedgrVo pLine = parseLine(rnum, line) ; 
				/* 지역코드 일치해야지만 사용 */ 
				String p_arcd  = pLine.getArcd() ; 
				String p_legcd = pLine.getLegcd() ;
				String p_arcode = p_arcd + p_legcd ; 
				/* 지역코드 일치해야지만 사용 */
				if (arcode.equals(p_arcode)) {
					wr.println(line) ;
//					mapper.insertHbdTitleLedgr(pLine) ;
					rnum ++ ; 
				}
			}			
			Log.info("*** job finihed: rows: {}", rnum - 1 );
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
	
	public int updated_htbdno( CommonMap params ) {
		int up_co = 0 ; 
		List<CommonMap> raList = mapper.selectRelatedMaster(params) ;
		if ( raList != null && raList.size() > 0 ) {
			for ( CommonMap map : raList ) {
				up_co = up_co + mapper.updateLedgrHtbdno(map) ; 
			}
		}
		
		return up_co ; 
	}
	
	@Transactional( rollbackFor = Exception.class )
	public void doExecute(CommonMap params ) {
		create_jobdir() ;
		read_ready_file( params ) ;
		int up_co = updated_htbdno( params ) ;
		Log.info("*** updated htbdno({})", up_co);
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
