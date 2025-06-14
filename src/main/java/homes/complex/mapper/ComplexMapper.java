package homes.complex.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.complex.vo.ComplexVo;

@Mapper
public interface ComplexMapper {

	public List<CommonMap> selectComplexList(ComplexVo paramVo) throws SQLException; 
	
}
