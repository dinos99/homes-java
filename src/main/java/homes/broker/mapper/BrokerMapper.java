package homes.broker.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerOfficeVo;
import homes.broker.vo.BrokerVo;

@Mapper
public interface BrokerMapper {
	public int insertBrokerUser(BrokerVo brokerVo) throws SQLException ;
	public long insertBrokerOffice(BrokerOfficeVo officeVo) throws SQLException ;
}
