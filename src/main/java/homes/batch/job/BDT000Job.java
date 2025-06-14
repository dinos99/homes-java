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
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BDT000Job implements Job {
	
	public Logger Log = LogManager.getLogger(BDT000Job.class) ;

	private final BatchMapper mapper ; 
	
	public final String BDT000 = EnumBatchJob.BASE_SUMMRY_RAWDATA.getCode() ; 
	
	public final String BTJOB_BASE_PATH   = HomesProperty.getPropVal("batch.job.base.path")  ; 
	public final String BTJOB_WAIT_PATH   = HomesProperty.getPropVal("batch.job.wait.path")  + File.separator + BDT000  ;
	public final String BTJOB_READY_PATH  = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BDT000  ; 
	
	public final String FILE_PRIFIX = "ready-" ; 
	public final String FILE_EXTENTION = ".txt" ; 

	public final String BTS_PROC  = "BTS001" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_DONE  = "BTS002" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	public final String BTS_ERROR = "BTS999" ; /* 작업실행상태 [BTS000: 대기, BTS001: 처리중, BTS002: 처리완료, BTS999: 에러] */ 
	
	public final int SPLIT_LINE = 10000;
	
	public String bt_uuid = "" ; 
	
	public void create_jobdir() {
		File wait_dir  = new File( BTJOB_WAIT_PATH ) ; 
		File ready_dir = new File( BTJOB_READY_PATH ) ; 
		
		if ( !wait_dir.isDirectory()) wait_dir.mkdirs() ; 
		if ( !ready_dir.isDirectory()) ready_dir.mkdirs() ; 
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
		Log.info("*** job file path: {}", BTJOB_READY_PATH + File.separator + FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		return new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION ) ;
	}
	
	public void move_ready() {
		/* ready folder 이동 */ 
		File wait_dir = new File( BTJOB_WAIT_PATH ) ;
		File f_ready = new File( BTJOB_READY_PATH + File.separator + FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION ) ;
//		Log.info("*** is directory for wait_dir ? => {}", wait_dir.isDirectory());
//		Log.info("*** is exist file for wait_dir only one => {}", wait_dir.list().length);

		if ( wait_dir.isDirectory() && wait_dir.list().length == 1 ) {
			String f_wait_nm = wait_dir.list()[0] ; 
//			Log.info("*** is exist file for wait_dir only one => file name is {}", f_wait_nm);
			File f_wait = new File(wait_dir.getAbsolutePath() + File.separator + f_wait_nm ) ;
			f_wait.renameTo(f_ready) ;
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
	
	@Transactional(rollbackFor = Exception.class)
	public int insertRawData() {
		int ins_co = 0 ; 
		FileReader fr = null ;
		BufferedReader br = null ;
		
		File jbfile = get_jobfile() ; 
		Long no = 1l ; 
		try {
			fr = new FileReader(jbfile ) ;
			br = new BufferedReader(fr) ;
			String line = "" ;
			while((line = br.readLine()) != null ) {
				CommonMap pMap = parseLine(line, no) ;
				mapper.insertBaseSummryRawData(pMap) ;	
				if ((no % SPLIT_LINE) == 1) {
					Log.info("inserted {} Lines", no) ;
				}
				no ++ ; 
			}
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;
		} catch ( IOException e ) {
        	Log.error("*** Batchfile Split Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		return ins_co ; 
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteRawData() {
		mapper.deleteBaseSummryRawdata(this.bt_uuid) ; 
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
		btmap.put("batchty", BDT000) ;
		btmap.put("sttuscd", BTS_PROC) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , FILE_PRIFIX + this.bt_uuid + FILE_EXTENTION) ;
		btmap.put("message", "[건축물 관리대장]기본개요 원시데이터를 생성합니다.") ;
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
		mapper.updateBatchjob(btmap) ;
	}
	public BatchVo doExecute(String batchYn) {
		/* 기본폴더 생성 */ 
		create_jobdir() ;
		String message = "[건축물관리대장]기본개요 원시데이터를 등록했습니다." ;
		message = check_jobFile() ;
		
		if ("EXISTS_JOB_FILE".equals(message)) {
			ready() ; 
			deleteRawData();
			insertRawData() ;
			done(BTS_DONE, 1, message) ; 
		}
		
		BatchVo btVo = new BatchVo(BDT000, batchYn) ;
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
        String batchYn     = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batchYn ) ;
		doExecute(batchYn) ; 
	}
}
