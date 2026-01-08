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
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumBatchJob;
import homes.comm.util.ObjectUtil;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import homes.data.vo.BaseOutLineVo;
import homes.data.vo.BaseSummaryVo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HBT000Job implements Job {
	
	public Logger Log = LogManager.getLogger(HBT000Job.class) ;

	private final BatchMapper mapper ; 
	
	public final String HBT000  = EnumBatchJob.MNG_BASE_SUMMRY.getCode() ; 
	public final String jobid   = EnumBatchJob.MNG_BASE_SUMMRY.getCode() ; 
	public final String jobnm   = EnumBatchJob.MNG_BASE_SUMMRY.getName() ;
	public final String bt_uuid = UUID.randomUUID().toString() ; 
	
	public BatchVo batchVo = new BatchVo(HBT000) ;
	
	@Transactional( rollbackFor = Exception.class )
	public int do_ready( CommonMap params ) {
		String batchde = params.getStringValue("batchde") ; 
		String message = "[" + jobid + "] " + jobnm + "작업을 준비중입니다." ;
		batchVo.setUuid(bt_uuid);
		batchVo.setBatchde(batchde);
		batchVo.setBatchAt("0");
		batchVo.setMessage(message); ; 
		int ex_co = mapper.insertBatchLog(batchVo) ; 
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
	public int insertBaseSummary(BaseOutLineVo outVo) {
		Long   mngrno  = batchVo.getMngrno() ; 
		String batchde = batchVo.getBatchde() ; 
		
		BaseSummaryVo sumVo = new BaseSummaryVo() ;
		String htbdno = mapper.getHomesHtbdno(outVo) ;
		String hbdno  = mapper.getHomesBuldno(outVo) ; 
		sumVo.setHtbdno(htbdno);
		sumVo.setHbdno(hbdno) ; 
		sumVo.setTotalRegstrPk(outVo.getTotalRegstrPk());
		sumVo.setBuldRegstrPk(outVo.getBuldRegstrPk());
		sumVo.setBatchde(batchde);
		sumVo.setBatchAt(EnumBatchJob.BATCH_AT_000.getCode()) ; 
		sumVo.setMngrno(mngrno);

		return mapper.insertBaseSummry(sumVo) ; 
	}

	@Transactional( rollbackFor = Exception.class )
	public int updateBatchSttus( BaseOutLineVo outVo ) {
		Long   mngrno  = batchVo.getMngrno() ; 
		String batchde = batchVo.getBatchde() ; 
		outVo.setBatchde(batchde);
		outVo.setMngrno(mngrno);
		outVo.setBfBatchAt(EnumBatchJob.BATCH_AT_000.getCode()); /* 이전상태: 작업대기 */
		outVo.setAfBatchAt(EnumBatchJob.BATCH_AT_010.getCode()); /* 변경 후 상태: 기본개요등록완료 */
		outVo.setBatchAt  (EnumBatchJob.BATCH_AT_000.getCode()); /* 현재상태: 작업대기 */
		/* 배치상태 업데이트 */ 
		return mapper.updateBaseOutLine(outVo) ; 
	}

	public BatchVo do_work( CommonMap params ) {
		String batchde = params.getStringValue("batchde") ;
		
		BaseOutLineVo pVo = new BaseOutLineVo() ; 
		pVo.setBatchde(batchde);
		pVo.setBatchAt(EnumBatchJob.BATCH_AT_000.getCode()) ;
		List<BaseOutLineVo> outList = mapper.selectBatchBaseOutLine(pVo) ;
		int ex_co = 0 ;
		if ( ObjectUtil.isNotEmpty(outList)) {
			for ( BaseOutLineVo outVo : outList ) {
				int has_co = mapper.getHbdBaseSummryCount(outVo) ;
				if ( has_co == 0 ) {
					/* Transaction을 위해 따로 분리 */
					ex_co = ex_co + insertBaseSummary(outVo) ; 
				} 
				/* Transaction을 위해 따로 분리 */
				updateBatchSttus(outVo) ;
			}
		}
		batchVo.setExco(ex_co);
		return this.batchVo ; 
	}

	public BatchVo doExecute( CommonMap params ) {		
		do_ready( params ) ;
//		do_work( params ) ;
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
