package homes.broker.service;

import java.sql.SQLException;
import java.util.List;

import homes.broker.vo.BrokerComplexVo;
import homes.comm.vo.CommonMap;

public interface BrokerComplexService {
	public List<CommonMap> selectComplexList(BrokerComplexVo paramVo) throws SQLException ; 
}
