package homes.broker.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerComplexVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BrokerComplexMapper {
	@Deprecated public List<CommonMap> selectMyComplexList(BrokerComplexVo paramVo) throws SQLException ; 
	@Deprecated public List<CommonMap> selectOthersComplexList(BrokerComplexVo paramVo) throws SQLException ;
	public List<CommonMap> selectTotalComplexList(BrokerComplexVo paramVo) throws SQLException ; 

	public CommonMap selectComplexinfo(BrokerComplexVo paramVo) ; 
	
	public String getBuldno(BrokerComplexVo paramVo) throws SQLException ; 
	
	public int insertBrokerComplex(BrokerComplexVo paramVo) throws SQLException ; 
	public int insertHbdBuldStuff(BrokerComplexVo paramVo) throws SQLException ; 
}