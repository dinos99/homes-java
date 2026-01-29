package homes.broker.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.broker.mapper.BrokerBuldMapper;
import homes.broker.mapper.BrokerStuffMapper;
import homes.broker.vo.BrokerBuldVo;
import homes.broker.vo.BrokerStuffVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrokerStuffServiceImpl implements BrokerStuffService {
	public final Logger Log = LogManager.getLogger(BrokerStuffServiceImpl.class) ;

	public final BrokerStuffMapper mapper ; 
	public final BrokerBuldMapper buldMapper ; 
	
	@Override
	@Transactional( readOnly = true)
	public List<CommonMap> getPublicHouseList(BrokerStuffVo paramVo) {
		paramVo.setBuldgb("2");
		return mapper.selectPublicHouseList(paramVo);
	}

	@Override
	@Transactional( rollbackFor = Exception.class )
	public int insertBrokerStuff(BrokerStuffVo paramVo) {
		/* STUFFNO 채번 */
		String stuffno = mapper.getStuffno(paramVo) ; 
		paramVo.setStuffno(stuffno);
		
		int ins_co = 0 ; 
		/* TB_BRK_STUFF 물건 등록 */
		ins_co = ins_co + mapper.insertBrokerStuff(paramVo);
		/* TB_BRK_STUFF_COMPLEX 물건_단지 등록 */		
		ins_co = ins_co + mapper.insertStuffComplex(paramVo);
		return ins_co ;
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class )
	public CommonMap updateBuldInfo(BrokerBuldVo paramVo) {
		int has_co = buldMapper.getBuldEtcCount(paramVo) ;
		int in_co = 0 ; 
		if ( has_co == 0 ) {
			in_co = buldMapper.insertBaseBuldEtc(paramVo) ; 
		}
		
		int up_co = buldMapper.updateBuldEtc(paramVo) ; 
		
		CommonMap cmap = new CommonMap() ; 
		cmap.put("insco",  in_co) ; 
		cmap.put("updco",  up_co) ; 
		return cmap ; 
	}

}
