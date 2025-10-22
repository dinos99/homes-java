package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.mapper.CommonMapper;
import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;
import homes.security.mapper.CommUserMapper;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

	public Logger Log = LogManager.getLogger(CommonServiceImpl.class) ; 
	
	private final CommonMapper mapper ;
	private final CommUserMapper commUserMapper ;
//	private final BrokerMapper brokerMapper ;

	@Override
	@Transactional(readOnly = true)
	public CommonMap selectArCode(String areacode ) throws SQLException {
		CommonMap armap =  mapper.selectArCode(areacode) ;
		List<CommonMap> arList =mapper.selectEmdList(armap.getStringValue("arcode")) ; 
		CommonMap arCode = new CommonMap() ; 
		arCode.put("arCode", armap) ;
		arCode.put("arList", arList) ;
		return arCode ; 
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectSidoList() throws SQLException {
		return mapper.selectSidoList() ; 
	}
	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectSggList( String sdcode ) throws SQLException {
		return mapper.selectSggList(sdcode) ; 
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectEmdList( String arcode ) throws SQLException {
		return mapper.selectEmdList(arcode) ; 
	}
	
	@Override
	@Transactional
	public Long insertCommuser(CommUserReqVo paramVo) throws SQLException {
		
		String pass = new String(Decoders.BASE64.decode(paramVo.getPassword())) ; 
		String cttpc = new String(Decoders.BASE64.decode(paramVo.getMbleCttpc())) ; 
		paramVo.setPassword(pass);
		paramVo.setMbleCttpc(cttpc);
		commUserMapper.insertCommuser(paramVo) ;
		Long userno = commUserMapper.getLastUserno(paramVo) ; 
//		Long userno = vo.getUserno() ; 
		Log.info("*** userno: {}", userno) ; 
		return userno ; 
		
		/* insert broker user */
		/*
		BrokerVo brokerVo = new BrokerVo() ;
		
		Long brokerno   = commUserMapper.selectLastid(0l) ;
		String brokernm = paramVo.getUserNm() ;
		String brokerty = Optional.ofNullable(paramVo.getBrokerty()).orElse("BRK002") ; 
		String brksttus = paramVo.getUserSttus() ; 
		
		brokerVo.setBrokerno(brokerno);
		brokerVo.setBrokernm(brokernm);
		brokerVo.setBrokerty(brokerty);
		brokerVo.setBrokersttus(brksttus) ; 
		
		insco = brokerMapper.insertBrokerUser(brokerVo) ;
		*/
	}

	@Override
	@Transactional(readOnly = true)
	public long selectLastid() throws SQLException {
		return commUserMapper.selectLastid(0l);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> getCommCodeList(String upcd) throws SQLException {
		return mapper.getCommCodeList(upcd) ;
	}
}
