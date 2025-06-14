package homes.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

/**
 * 건축물대장등록-기본개요_원본 등록
 * Registration TB_BD_BASESUMMRY_ORIGIN Table. 
 */
@Component
@RequiredArgsConstructor
public class BLD001Job implements Job {
	
	public Logger Log = LogManager.getLogger(BLD001Job.class) ;
	private final BatchMapper mapper ;
	
	public final String BJT003    = EnumBatchJob.BJT003.getCode() ;
	
	public final String BLD001    = EnumBatchJob.BLD001.getCode() ;	
	
	public final String BTS_PROC  = EnumBatchJob.BTS001.getCode() ; 
	public final String BTS_DONE  = EnumBatchJob.BTS002.getCode() ;  
	public final String BTS_ERROR = EnumBatchJob.BTS999.getCode() ;  

	public final String BTJOB_BASE_PATH  = HomesProperty.getPropVal("batch.job.base.path")  + File.separator + BLD001 ; 
	public final String BTJOB_DONE_PATH  = HomesProperty.getPropVal("batch.job.done.path")  + File.separator + BLD001 ;
	public final String BTJOB_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BLD001 ;
	
	public final String BTJOB_BTJ003_READY_PATH = HomesProperty.getPropVal("batch.job.ready.path") + File.separator + BJT003 ;

	public final String SP_FILE_PREFIX     = "SP-" ; 
	public final String ORIGIN_FILE_PREFIX = "ORIGIN-" ; 
	public final String SP_FILE_EXTENTION  = ".txt" ; 

	public final int SPLIT_LINE = 1000 ; 
	public String bt_uuid = "" ; 
	public String arcode  = "" ; 
	public String sdcode  = "" ; 
	public String arname  = "" ;
	
	public void createJobDir() {
		File dir_base  = new File( BTJOB_BASE_PATH ) ; 
		File dir_done  = new File( BTJOB_DONE_PATH ) ; 
		File dir_ready = new File( BTJOB_BTJ003_READY_PATH ) ; 
		
		if ( !dir_base.isDirectory()) dir_base.mkdirs() ;
		if ( !dir_done.isDirectory()) dir_done.mkdirs() ;
		if ( !dir_ready.isDirectory()) dir_ready.mkdirs() ;
		
	}
	
	public void createSidoDir(String sdcd) {
		this.sdcode = sdcd ; 
		String ready_003 = BTJOB_BTJ003_READY_PATH + File.separator + this.sdcode ;
		File dir_sdcode = new File( ready_003  ) ;
		if ( !dir_sdcode.isDirectory()) dir_sdcode.mkdirs() ; 
	}


	public File moveTargetFile(String sdcd, String arcd, String arnm) {
		
		this.sdcode = sdcd ;
		this.arcode = arcd ;
		this.arname = arnm ;
		Log.info("sdcode: {}, arcode: {}, arname: {}", this.sdcode, this.arcode, this.arname );
		createJobDir() ;
		createSidoDir(this.sdcode)  ; 

		File dir_bjt003_ready = new File( BTJOB_BTJ003_READY_PATH + File.separator + this.sdcode  ) ;
		String f_nm = "" ; 
		for ( String src_nm : dir_bjt003_ready.list()) {
			String s_arcd = getArcode(src_nm) ;
			if ( this.arcode.equals(s_arcd)) {
				f_nm = src_nm ;
				this.bt_uuid = getUUID(f_nm) ; 
				break ;
			}
		}
		
		File src  = new File( BTJOB_BTJ003_READY_PATH + File.separator + this.sdcode + File.separator + f_nm ) ; 
		if ( src.isFile()) {
			String mv_fnm = SP_FILE_PREFIX + BLD001 + "-" + this.bt_uuid + "-" + this.arcode + SP_FILE_EXTENTION ;
			File dest = new File( BTJOB_READY_PATH + File.separator + mv_fnm ) ;
			Log.error("*** bt_uuid           : {}", this.bt_uuid);
			Log.error("*** move to file from : {} ", BTJOB_BTJ003_READY_PATH + File.separator + this.sdcode + File.separator +  f_nm) ;
			Log.error("*** move to file to   : {} ", BTJOB_READY_PATH  + File.separator + mv_fnm) ;
			src.renameTo(dest) ; 
			return dest ; 
		}
		return null ; 
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
	
	public String getUUID( String f_nm ) {
		this.bt_uuid = f_nm.substring(10, 46) ;
		return this.bt_uuid ; 
	}
	
	public String getArcode( String f_nm ) {
		int idx_ext = f_nm.indexOf(".") ;  
		return f_nm.substring(idx_ext - 5, idx_ext ) ;
	}

	@Transactional(rollbackFor = Exception.class)
	public void start(File mv_file) {
		CommonMap btmap = new CommonMap() ; 
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , mv_file.getName()) ; 
		btmap.put("batchty", BLD001) ;
		btmap.put("sttuscd", BTS_PROC ) ;
		btmap.put("message", "[" + this.arname + "]지역 기본개요등록을 시작하였습니다.") ; 
		mapper.insertBatchjob(btmap) ; 
	}

	@Transactional(rollbackFor = Exception.class)
	public void finished(int item_co, File mv_file) {
		CommonMap btmap = new CommonMap() ; 
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("exco"   , item_co) ;
		btmap.put("filenm" , mv_file.getName()) ; 
		btmap.put("batchty", BLD001) ;
		btmap.put("sttuscd", BTS_DONE ) ; 
		btmap.put("message",  "[" + this.arname + "]지역 등록였습니다.(" + StringUtil.getCurrencyFormat(item_co) + ")건") ; 
		mapper.updateBatchjob(btmap) ; 
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteBaseSummry(String arcode) {
		return mapper.deleteBaseSummry(arcode) ;
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertBaseSummry(String rowdata, String batchYn) {
		String[] cols  = this.getHeader() ;
		String[] datas = rowdata.split("[|]") ;
		CommonMap pMap = new CommonMap() ;
		for ( int i = 0; i < datas.length; i ++ ) {
			String column = cols[i] ; 
			String data   = datas[i] ;

			if ("outLotCo".equals(column) ||"nwAddrMainB".equals(column) || "nwAddrSubB".equals(column)) {
				int iVal = StringUtil.getIntValue(data) ;
				pMap.put(column, iVal) ;
			} else {
				pMap.put(column, data) ;
			}
		}
		pMap.put("batchYn", batchYn) ;
		return mapper.insertBaseSummry(pMap) ;
	}
	
	public int doParse(File file, BatchVo batVo) {
		CommonMap btmap = new CommonMap() ;
		FileReader filereader = null ;
		BufferedReader bufReader = null ;
		int ex_co  = 0 ; 
		try {
			filereader = new FileReader(file) ;
			bufReader = new BufferedReader(filereader) ;
			this.deleteBaseSummry(this.arcode) ; 
			String line = "" ;
			while((line = bufReader.readLine()) != null) {
				this.insertBaseSummry(line, batVo.getBatchYn()) ;
				ex_co ++ ;
				Log.error("*** exco : {}, batch file: {}", ex_co, file.getName()) ;
			}
			bufReader.close() ;
			filereader.close()  ; 

			File done_file = new File(BTJOB_DONE_PATH +  File.separator + "done-" + file.getName()) ;
			Log.error(" *** batch done path  : {}", BTJOB_DONE_PATH) ; 
			Log.error(" *** batch source file: {}", file.getAbsolutePath()) ;
			Log.error(" *** batch done   file: {}", done_file.getAbsolutePath()) ; 
			file.renameTo(done_file) ; 
			
		} catch ( IOException e ) {
			btmap.put("sttuscd", BTS_ERROR) ; 
			btmap.put("filenm" , "") ;
			btmap.put("exco"   , 0) ;
			btmap.put("message", "[IO_EXCEPTION] 파일 입출력에러") ;
			btmap.put("filenm" , file.getName()) ;
			mapper.updateBatchjob(btmap) ;
			Log.error("*** Batch Error: IOException: [{}][{}]:{}", BLD001, bt_uuid, e.getMessage()) ;
		} finally {
			try { if ( bufReader != null ) bufReader.close() ; } catch ( IOException e ) { bufReader = null ; }
			try { if ( filereader != null ) filereader.close() ; } catch ( IOException e ) { filereader = null ; };
		}
		return ex_co ;
	}
	
	public int doExecute(BatchVo batVo) {
		Log.info("*** execute batchjob, jobid [ {} ] started at {}", BLD001, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		int ex_co = 0 ;
		List<CommonMap> sdList = mapper.selectSidocodeList("") ; 
		for ( CommonMap sdmap : sdList ) {
			String sdcode = Optional.ofNullable((String)sdmap.get("sdcode")).orElse("11") ;
			
			List<CommonMap> sggList = mapper.selectSggcodeList(sdcode) ;
			sdcode = sdcode + "000" ;
			for ( CommonMap sggmap : sggList ) {
				String arcode = Optional.ofNullable((String)sggmap.get("arcode")).orElse("11000") ;
				String arname = Optional.ofNullable((String)sggmap.get("arname")).orElse("서울특별시") ;
				File mv_file = moveTargetFile(sdcode, arcode, arname) ; 
				if ( mv_file != null ) {
					start(mv_file) ; 
					ex_co = doParse(mv_file, batVo) ;
					finished(ex_co, mv_file) ;
					break ;
				} else {
					continue ;
				}
			} 
			break ;
		}
		Log.info("*** executed batchjob, jobid [ {}({}) ] finished at {}", BLD001, ex_co, DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
		return ex_co ; 
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		BatchVo batVo = new BatchVo(BLD001, "Y") ; 
        doExecute(batVo) ;
	} 	

}
