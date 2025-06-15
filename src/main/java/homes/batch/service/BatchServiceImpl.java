package homes.batch.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.job.BDT000Job;
import homes.batch.job.BDT001Job;
import homes.batch.job.BJT000Job;
import homes.batch.job.BJT001Job;
import homes.batch.job.BJT002Job;
import homes.batch.job.BJT003Job;
import homes.batch.job.BLD001Job;
import homes.batch.job.BLD002Job;
import homes.batch.job.BLD003Job;
import homes.batch.job.BLD009Job;
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
	

	private final String BDT000 = EnumBatchJob.SPLIT_BASE_SUMMRY_RAWDATA.getCode() ; 
	private final String BDT001 = EnumBatchJob.INSERT_BASE_SUMMRY_RAWDATA.getCode() ; 

	private final BDT000Job job_bdt_000 ; 
	private final BDT001Job job_bdt_001 ; 
	
	private final BJT000Job job_000 ;
	private final BJT001Job job_001 ;
	private final BJT002Job job_002 ;
	private final BJT003Job job_003 ;

	private final BLD001Job job_buld_001 ;
	private final BLD002Job job_buld_002 ;
	private final BLD003Job job_buld_003 ;
	private final BLD009Job job_buld_009 ;
	
	private final BatchMapper mapper ;

	private final String BJT000 = EnumBatchJob.BJT000.getCode() ;
	private final String BJT001 = EnumBatchJob.BJT001.getCode() ;
	private final String BJT002 = EnumBatchJob.BJT002.getCode() ;
	private final String BJT003 = EnumBatchJob.BJT003.getCode() ;
	
	private final String BLD001 = EnumBatchJob.BLD001.getCode() ; 
	private final String BLD002 = EnumBatchJob.BLD002.getCode() ; 
	private final String BLD003 = EnumBatchJob.BLD003.getCode() ; 
//	private final String BLD005 = EnumBatchJob.BLD005.getCode() ; 
	private final String BLD009 = EnumBatchJob.BLD009.getCode() ; 
	
	@Override
	public BatchVo doExecute(BatchVo paramVo) {
		String  jobid   = paramVo.getJobid() ; 
		String  batchYn = paramVo.getBatchYn() ;  
		BatchVo btVo    = new BatchVo(jobid, batchYn) ; 
		
		if ( BDT000.equals(jobid)) { 
			btVo = job_bdt_000.doExecute("N");
		} else if ( BDT001.equals(jobid)) { 
			btVo = job_bdt_001.doExecute("N");
		} else if ( BJT000.equals(jobid)) {
			int exco = job_000.doExecute() ;
			btVo.setExco(exco);
		} else if ( BJT001.equals(jobid)) {
			int exco = job_001.doExecute() ;
			btVo.setExco(exco);
		} else if ( BJT002.equals(jobid)) {
			int sp_co = job_002.doExecute() ;
			btVo.setExco(sp_co); 
		} else if ( BJT003.equals(jobid)) { 
			int sp_co = job_003.doExecute() ;
			btVo.setExco(sp_co); 
		} else if ( BLD001.equals(jobid)) {
			int exco = job_buld_001.doExecute(btVo) ;
			btVo.setExco(exco);
		} else if ( BLD002.equals(jobid)) {
			int exco = job_buld_002.doExecute(btVo) ;
			btVo.setExco(exco);
		} else if ( BLD003.equals(jobid)) {
			int exco = job_buld_003.doExecute(btVo) ;
			btVo.setExco(exco);
		} else if ( BLD009.equals(jobid)) {
			int exco = job_buld_009.doExecute(btVo) ;
			btVo.setExco(exco);
		}
		return btVo;
	}

	@Override
	@Transactional( readOnly = true )
	public CommResponseVo selectBatchJobList( BatchReqVo paramVo) throws SQLException {
		Log.info("*** pageno: {}", paramVo.getPgno()) ;
		paramVo.setPage(); 
		Long t_cnt = mapper.selectBatchJobListCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectBatchJobList(paramVo) ;
		return new CommResponseVo(t_cnt, paramVo.getPgno(), null, dataList) ;
	}

}
