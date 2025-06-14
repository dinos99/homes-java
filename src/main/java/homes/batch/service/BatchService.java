package homes.batch.service;

import java.sql.SQLException;

import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.vo.CommResponseVo;

public interface BatchService {
	public BatchVo doExecute(BatchVo paramVo ) ; 
	
	public CommResponseVo selectBatchJobList( BatchReqVo paramVo) throws SQLException ; 
	
}
