package homes.broker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerStuffVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BrokerStuffMapper {
	/* 물건에 등록할 단지(공동주택)목록을 조회한다. */
	List<CommonMap> selectPublicHouseList ( BrokerStuffVo paramVo ) ; 
	/* 중개사무소 물건에 단지(공동주택)을 추가한다. */
	int insertBrokerStuff ( BrokerStuffVo paramVo ) ; 
}