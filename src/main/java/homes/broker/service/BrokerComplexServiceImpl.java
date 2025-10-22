package homes.broker.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.broker.mapper.BrokerComplexMapper;
import homes.broker.vo.BrokerComplexVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrokerComplexServiceImpl implements BrokerComplexService {
	public final Logger Log = LogManager.getLogger(BrokerComplexServiceImpl.class) ;

	private final BrokerComplexMapper mapper ;

	@Override
	@Transactional( readOnly = true )
	public CommonMap selectComplexinfo(BrokerComplexVo paramVo) {
		return mapper.selectComplexinfo(paramVo) ;
	}
	
	@Override
	@Transactional( readOnly = true )
	public CommonMap selectComplexList(BrokerComplexVo paramVo) throws SQLException {
		CommonMap cpmap = new CommonMap() ; 
		List<CommonMap> tComplex = mapper.selectTotalComplexList(paramVo) ;
		cpmap.put("tComplex", tComplex) ; 
		return cpmap ; 
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertBrokerComplex(BrokerComplexVo paramVo) throws SQLException {
		return mapper.insertBrokerComplex(paramVo) ;
	}
	
}
