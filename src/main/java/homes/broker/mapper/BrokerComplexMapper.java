package homes.broker.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerComplexVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BrokerComplexMapper {
	public List<CommonMap> selectComplexList(BrokerComplexVo paramVo) throws SQLException ; 
}
