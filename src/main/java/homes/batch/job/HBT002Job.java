package homes.batch.job;

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
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.data.vo.LedgrVo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HBT002Job implements Job {
	
	public Logger Log = LogManager.getLogger(HBT002Job.class) ;

	private final BatchMapper mapper ; 

	public final String HBT002  = EnumBatchJob.MNG_LEDGR.getCode() ; 
	public final String jobid   = EnumBatchJob.MNG_LEDGR.getCode() ; 
	public final String jobnm   = EnumBatchJob.MNG_LEDGR.getName() ;
	public final String bt_uuid = UUID.randomUUID().toString() ; 
	
	public BatchVo batchVo = new BatchVo(HBT002) ;
	
	@Transactional( rollbackFor = Exception.class )
	public int do_ready( CommonMap params ) {
		String batchde = params.getStringValue("batchde") ;
		String batchat = EnumBatchJob.BATCH_AT_010.getCode(); 
		
		LedgrVo pLedgrVo = new LedgrVo() ;
		pLedgrVo.setBatchde(batchde);
		pLedgrVo.setBatchAt(batchat);
		pLedgrVo.setMngrno(batchVo.getMngrno());
		int ex_co = mapper.getHbdLedgrCount(pLedgrVo) ; 
		batchVo.setExco(ex_co);
		
		String message = "[" + jobid + "] " + jobnm + "작업을 준비중입니다. 대상건수: " ;
		message += StringUtil.getCurrencyFormat(batchVo.getExco()) + "건" ;
		
		batchVo.setUuid(bt_uuid);
		batchVo.setBatchde(batchde);
		batchVo.setBatchAt(batchat);
		batchVo.setMessage(message); ;
		mapper.insertBatchLog(batchVo) ; 
		batchVo.setExco(ex_co);
		Log.info("*** [{}][{}]:{} is ready ", bt_uuid, jobid, jobnm ) ;
		return ex_co ; 
	}
	
	@Transactional( rollbackFor = Exception.class )
	public int do_finish( CommonMap params ) {
		String message = "[" + jobid + "] " + jobnm + "작업이 종료되었습니다. 작업건수: " ;
		message += StringUtil.getCurrencyFormat(batchVo.getExco()) + "건" ;
		batchVo.setSttuscd("BTS002");
		batchVo.setMessage(message);
		int ex_co = mapper.updateBatchLog(batchVo) ;
		Log.info("*** [{}][{}]:{} is done ", bt_uuid, jobid, jobnm ) ;
		return ex_co ; 
	}

	@Transactional( rollbackFor = Exception.class )
	public BatchVo do_work( CommonMap params ) {
		String batchde = params.getStringValue("batchde") ;
		String batchat = EnumBatchJob.BATCH_AT_010.getCode(); 
		String af_batchat = EnumBatchJob.BATCH_AT_030.getCode(); 
		LedgrVo pLedgrVo = new LedgrVo() ;
		pLedgrVo.setBatchde(batchde);
		pLedgrVo.setBatchAt(batchat);
		pLedgrVo.setAfBatchAt(af_batchat);
		pLedgrVo.setMngrno(batchVo.getMngrno());
		pLedgrVo.setCfmvgb("1");
		int ex_co = mapper.insertLedgr(pLedgrVo) ; 
		batchVo.setExco(ex_co) ;
		return this.batchVo ; 
	}
	
	public BatchVo doExecute( CommonMap params ) {		
		do_ready( params ) ;
		do_work( params ) ;
		do_finish( params ) ;
		return this.batchVo ; 
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
        String batchYn     = Optional.ofNullable((String) dataMap.get("batchYn")).orElse("N");
        Log.info("*** is batch: {}", batchYn ) ;
//		doExecute(batchYn) ; 
	}
}
