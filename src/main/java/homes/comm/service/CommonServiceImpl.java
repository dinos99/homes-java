package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.mapper.CommonMapper;
import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

	public Logger Log = LogManager.getLogger(CommonServiceImpl.class) ; 
	
	private final CommonMapper mapper ;

	@Override
	public List<CommonMap> selectSidoList(CommReqVo paramVo) throws SQLException {
		return mapper.selectSidoList(paramVo);
	}

	@Override
	public List<CommonMap> selectSggList(CommReqVo paramVo) throws SQLException {
		return mapper.selectSidoList(paramVo);
	}
	
	@Override
	public List<CommonMap> selectEmdList(CommReqVo paramVo) throws SQLException {
		return mapper.selectEmdList(paramVo);
	}

	@Override
	public CommonMap selectArcodeList(CommReqVo paramVo) throws SQLException {
		List<CommonMap> arSidoList = mapper.selectSidoList(paramVo) ; 
		List<CommonMap> arSggList  = mapper.selectSggList(paramVo) ;
		List<CommonMap> arEmdList  = mapper.selectEmdList(paramVo) ;
		CommonMap arList = new CommonMap() ; 
		arList.put("arSidoList",arSidoList) ; 
		arList.put("arSggList" ,arSggList) ; 
		arList.put("arEmdList" ,arEmdList) ; 
		return arList;
	} 
}
