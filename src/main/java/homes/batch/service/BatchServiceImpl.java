package homes.batch.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.job.HBT000Job;
import homes.batch.job.HBT001Job;
import homes.batch.job.HBT002Job;
import homes.batch.mapper.BatchMapper;
import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumBatchJob;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
	public final Logger Log = LogManager.getLogger(BatchServiceImpl.class) ;
	
	private final String HBT000 = EnumBatchJob.MNG_BASE_SUMMRY.getCode() ; 
	private final String HBT001 = EnumBatchJob.MNG_LEDGR_MSTR.getCode() ; 
	private final String HBT002 = EnumBatchJob.MNG_LEDGR.getCode() ; 
	
	private final HBT000Job job_hbt_000 ; /* 홈즈 건물관리대장 개본개요   작업 */ 
	private final HBT001Job job_hbt_001 ; /* 홈즈 건물관리대장 총괄표제부 작업 */ 
	private final HBT002Job job_hbt_002 ; /* 홈즈 건물관리대장 표제부     작업 */ 
			
	private final BatchMapper mapper ;

	private BatchVo batchVo ; 

	@Override
	public BatchVo doExecute(String jobid, CommonMap params) {
		if ( HBT000.equals(jobid)) {
			batchVo = job_hbt_000.doExecute(params) ; 
		} else if ( HBT001.equals(jobid)) {
			batchVo = job_hbt_001.doExecute(params) ; 
		} else if ( HBT002.equals(jobid)) {
			batchVo = job_hbt_002.doExecute(params) ; 
		}
		
		return this.batchVo ; 
	}
	
	@Override
	@Transactional( readOnly = true )
	public CommResponseVo selectBatchJobList( BatchReqVo paramVo ) throws SQLException {
		paramVo.setPage(); 
		Long t_cnt = mapper.selectBatchJobListCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectBatchJobList(paramVo) ;
		return new CommResponseVo(t_cnt, paramVo.getPgno(), null, dataList) ;
	}

	@Override
	public BatchVo doExecute(BatchVo paramVo, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
