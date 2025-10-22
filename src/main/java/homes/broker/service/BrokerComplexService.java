package homes.broker.service;

import java.sql.SQLException;

import homes.broker.vo.BrokerComplexVo;
import homes.comm.vo.CommonMap;

public interface BrokerComplexService {
	public CommonMap selectComplexinfo(BrokerComplexVo paramVo) ; 	
	public CommonMap selectComplexList(BrokerComplexVo paramVo) throws SQLException ; 	
	public int insertBrokerComplex(BrokerComplexVo paramVo) throws SQLException ;
}
