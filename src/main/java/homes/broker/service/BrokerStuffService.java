package homes.broker.service;

import java.util.List;

import homes.broker.vo.BrokerStuffVo;
import homes.comm.vo.CommonMap;

public interface BrokerStuffService {
	
	/* 물건에 등록할 단지(공동주택)목록을 조회한다. */ 
	List<CommonMap> getPublicHouseList( BrokerStuffVo paramVo ) ;
	/* 중개사무소 물건에 단지(공동주택)을 추가한다. */
	int insertBrokerStuff(BrokerStuffVo paramVo) ; 
	
}
