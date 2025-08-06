package homes.broker.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

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
	public List<CommonMap> selectComplexList(BrokerComplexVo paramVo) throws SQLException {
		return mapper.selectComplexList(paramVo) ;
	}
	
}
