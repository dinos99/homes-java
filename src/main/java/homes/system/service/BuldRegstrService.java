package homes.system.service;

import java.sql.SQLException;

import homes.comm.vo.CommResponseVo;
import homes.system.vo.BuldRegstrReqVo;

public interface BuldRegstrService {

	public CommResponseVo selectTotalLedgr( BuldRegstrReqVo paramVo) throws SQLException ; 
}
