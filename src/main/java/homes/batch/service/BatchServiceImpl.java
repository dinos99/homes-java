package homes.batch.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.job.BDT000Job;
import homes.batch.job.BDT001Job;
import homes.batch.job.BDT021Job;
import homes.batch.job.BDT030Job;
import homes.batch.job.BDT031Job;
import homes.batch.job.BDT040Job;
import homes.batch.job.BDT041Job;
import homes.batch.mapper.BatchMapper;
import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumBatchJob;
import homes.comm.util.JwtUtil;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
	public final Logger Log = LogManager.getLogger(BatchServiceImpl.class) ;
	

	private final String BDT000 = EnumBatchJob.SPLIT_BASE_SUMMRY.getCode() ; 
	private final String BDT001 = EnumBatchJob.INSERT_BASE_SUMMRY.getCode() ; 
	private final String BDT021 = EnumBatchJob.INSERT_TOTAL_LEDGER.getCode() ; 
	private final String BDT030 = EnumBatchJob.SPLIT_TITLE_LEDGER.getCode() ; 
	private final String BDT031 = EnumBatchJob.INSERT_TITLE_LEDGER.getCode() ; 
	private final String BDT040 = EnumBatchJob.SPLIT_PSSION_LEDGER.getCode() ; 
	private final String BDT041 = EnumBatchJob.INSERT_PSSION_LEDGER.getCode() ; 

	private final BDT000Job job_bdt_000 ; /* 기본개요   파일분할(Local job) */ 
	private final BDT001Job job_bdt_001 ; /* 기본개요   등록 */ 
	private final BDT021Job job_bdt_021 ; /* 총괄표제부 등록 */ 
	private final BDT030Job job_bdt_030 ; /* 표제부     파일분할(Local job) */ 
	private final BDT031Job job_bdt_031 ; /* 표제부     등록 */ 
	private final BDT040Job job_bdt_040 ; /* 전유부     파일분할(Local job) */ 
	private final BDT041Job job_bdt_041 ; /* 전유부     등록 */ 
		
	private final BatchMapper mapper ;
	
	@Override
	public BatchVo doExecute(BatchVo paramVo, String token) {
		String  jobid   = paramVo.getJobid() ; 
		String  batchYn = paramVo.getBatchYn() ;  

		JwtUtil tkUtil = new JwtUtil() ;
		Long mngrno = tkUtil.getUserId(token) ; 
		BatchVo btVo    = new BatchVo(jobid, batchYn, mngrno) ; 
		
		if ( BDT000.equals(jobid)) { 
			btVo = job_bdt_000.doExecute("N") ;
		} else if ( BDT001.equals(jobid)) { 
			btVo = job_bdt_001.doExecute("N") ;
		} else if ( BDT021.equals(jobid)) {
			btVo = job_bdt_021.doExecute("N") ;
			btVo = job_bdt_001.doExecute("N") ;
		} else if ( BDT030.equals(jobid)) {
			btVo = job_bdt_030.doExecute("N") ;
		} else if ( BDT031.equals(jobid)) {
			btVo = job_bdt_031.doExecute("N", mngrno) ;
		} else if ( BDT040.equals(jobid)) {
			btVo = job_bdt_040.doExecute("N", mngrno) ;
		} else if ( BDT041.equals(jobid)) {
			btVo = job_bdt_041.doExecute("N", mngrno) ;
		} else {
			btVo.setExco(0);
			btVo.setSpco(0);
			btVo.setMessage("작업ID[" + jobid + "]를 확인 해 주세요");
		}
		return btVo;
	}

	@Override
	@Transactional( readOnly = true )
	public CommResponseVo selectBatchJobList( BatchReqVo paramVo ) throws SQLException {
		paramVo.setPage(); 
		Long t_cnt = mapper.selectBatchJobListCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectBatchJobList(paramVo) ;
		return new CommResponseVo(t_cnt, paramVo.getPgno(), null, dataList) ;
	}

}
