package homes.broker.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.broker.mapper.BrokerStuffMapper;
import homes.broker.vo.BrokerStuffVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrokerStuffServiceImpl implements BrokerStuffService {
	public final Logger Log = LogManager.getLogger(BrokerStuffServiceImpl.class) ;

	public final BrokerStuffMapper mapper ; 
	
	@Override
	@Transactional( readOnly = true)
	public List<CommonMap> getPublicHouseList(BrokerStuffVo paramVo) {
		paramVo.setBuldgb("2");
		return mapper.selectPublicHouseList(paramVo);
	}

	@Override
	@Transactional( rollbackFor = Exception.class )
	public int insertBrokerStuff(BrokerStuffVo paramVo) {
		paramVo.setSfsttus("T") ; 
		return mapper.insertBrokerStuff(paramVo);
	}

}
