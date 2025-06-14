package homes.complex.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommonMap;
import homes.complex.vo.ComplexVo;

public interface ComplexService {
	
	/* 단지목록조회 */
	public List<CommonMap> selectComplexList(ComplexVo paramVo) throws SQLException ; 
}
