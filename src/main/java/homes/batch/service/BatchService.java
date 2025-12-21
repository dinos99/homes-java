package homes.batch.service;

import java.sql.SQLException;

import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;

public interface BatchService {
	public BatchVo doExecute( String jobid, CommonMap params ) ; 
	public BatchVo doExecute( BatchVo paramVo, String token ) ; 
	
	public CommResponseVo selectBatchJobList( BatchReqVo paramVo ) throws SQLException ; 
	
}
