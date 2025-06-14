package homes.complex.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import homes.comm.vo.CommonMap;
import homes.complex.mapper.ComplexMapper;
import homes.complex.vo.ComplexVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplexServiceImpl implements ComplexService {
	public Logger Log = LogManager.getLogger(ComplexServiceImpl.class) ;

	private final ComplexMapper mapper ;
	
	@Override
	public List<CommonMap> selectComplexList(ComplexVo paramVo) throws SQLException {
		return mapper.selectComplexList(paramVo);
	} 
}
