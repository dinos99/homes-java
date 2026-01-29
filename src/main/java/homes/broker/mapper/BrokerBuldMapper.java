package homes.broker.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.broker.vo.BrokerBuldVo;

@Mapper
public interface BrokerBuldMapper {
	int getBuldEtcCount( BrokerBuldVo paramVo ) ;
	int insertBaseBuldEtc( BrokerBuldVo paramVo ) ;
	int updateBuldEtc( BrokerBuldVo paramVo ) ;
}