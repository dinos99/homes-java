package homes.broker.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerMemoVo;
import homes.broker.vo.BrokerOfficeVo;
import homes.broker.vo.BrokerVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BrokerMapper {
	public int insertBrokerUser(BrokerVo brokerVo) throws SQLException ;
	public long insertBrokerOffice(BrokerOfficeVo officeVo) throws SQLException ;	
	public String isExistBroker( long userno ) throws SQLException ; 
	
	public int insertBrkMemo( BrokerMemoVo memoVo ) ;
	
	public List<BrokerMemoVo> selectBrokerMemo( BrokerMemoVo memoVo ) ;
	public CommonMap selectBrokerMemoCount( BrokerMemoVo memoVo ) ;
	
	
}
