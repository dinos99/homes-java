package homes.batch.job;

import java.util.List;
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
import homes.comm.constants.EnumBatchJob;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ARC000Job implements Job {
	public Logger Log = LogManager.getLogger(ARC000Job.class) ;

	private final BatchMapper mapper ; 
	
	public String bt_uuid = "" ; 
	public String batchYn = "" ; 
	public Long   mngrno  = 0l ; 

	public final String ARC000 = EnumBatchJob.CONV_LEGCD_NULL_TO_CODE_ARC000.getCode() ;

	/* *********************************
	 * 작업준비 시작
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void ready(String batYn, Long mno ) {
	    this.batchYn = batYn ;
	    this.mngrno  = mno ;
		this.bt_uuid = UUID.randomUUID().toString() ;

		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("batchty", ARC000) ;
		btmap.put("sttuscd", EnumBatchJob.BTS_PROC.getCode()) ;
		btmap.put("exco"   , 0) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", EnumBatchJob.CONV_LEGCD_NULL_TO_CODE_ARC000.getName() + "을 시작합니다.") ;
		btmap.put("mngrno" , this.mngrno) ;
		mapper.insertBatchjob(btmap) ;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Long doConvert() {
		Long ex_co = 0l ;
		List<CommonMap> arTargetList = mapper.selectNullLegcdList() ; 
		if ( arTargetList != null && arTargetList.size() > 0) {
			for ( CommonMap tmap : arTargetList ) {
				String arcd = Optional.ofNullable(String.valueOf(tmap.get("arcd"))).orElse("") ; 
				List<CommonMap> arList = mapper.selectConvArcodeList(arcd) ; 
				if ( arList != null && arList.size() > 0) {
					for ( CommonMap armap: arList) {
						int up_co = mapper.updateLegcdTotalLedger(armap) ;
						if ( up_co == 0 ) {
							Log.error("update faild: {}", armap.getStringValue("arcode"));
							mapper.deleteLegcdTotalLedger(armap.getStringValue("arcode")) ;
						}
						ex_co += up_co ;
					}
				}
			}
		}
		
		return ex_co ; 
	} 
	/* *********************************
	 * 작업완료 
	 * *********************************/
	@Transactional(rollbackFor = Exception.class) 
	public void done(String sttus, Long exco, String message) {
		CommonMap btmap = new CommonMap() ;
		btmap.put("uuid"   , this.bt_uuid) ;
		btmap.put("sttuscd", sttus) ;
		btmap.put("exco"   , exco) ;
		btmap.put("filenm" , "") ;
		btmap.put("message", message) ;
		btmap.put("mngrno" , this.mngrno) ;
		mapper.updateBatchjob(btmap) ;
	}
	
	public void doExecute(String batYn, Long mno) {
	    Long up_co = 0l ;
		List<CommonMap> arTargetList = mapper.selectNullLegcdList() ;
		String message = "변경대상이 없습니다." ; 
		if ( arTargetList != null && arTargetList.size() > 0) {
		    ready(batYn, mno) ;
		    up_co = doConvert() ;
		    message = EnumBatchJob.CONV_LEGCD_NULL_TO_CODE_ARC000.getName() + "완료("
			        + StringUtil.getCurrencyFormat(up_co, "#,###") + ")" ;
		    
		    done(EnumBatchJob.BTS_DONE.getCode(), up_co, message) ;
		}
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
	    String batYn  = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
	    String mngrno = Optional.ofNullable(String.valueOf(dataMap.get("userno"))).orElse("0");
	    this.batchYn = batYn ;
	    this.mngrno  = Long.parseLong(mngrno) ;
	    
	    doExecute(batYn, Long.parseLong(mngrno)) ; 
	    
	}
	


}
