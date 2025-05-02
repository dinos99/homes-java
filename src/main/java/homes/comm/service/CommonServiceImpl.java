package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.mapper.CommonMapper;
import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;
import homes.security.mapper.CommUserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

	public Logger Log = LogManager.getLogger(CommonServiceImpl.class) ; 
	
	private final CommonMapper mapper ;
	private final CommUserMapper commUserMapper ;

	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectSidoList(CommReqVo paramVo) throws SQLException {
		return mapper.selectSidoList(paramVo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectSggList(CommReqVo paramVo) throws SQLException {
		return mapper.selectSidoList(paramVo);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectEmdList(CommReqVo paramVo) throws SQLException {
		return mapper.selectEmdList(paramVo);
	}

	@Override
	@Transactional(readOnly = true)
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

	@Override
	@Transactional
	public int insertCommuser(CommUserReqVo paramVo) throws SQLException {
		return commUserMapper.insertCommuser(paramVo) ;
	}

	@Override
	@Transactional(readOnly = true)
	public long selectLastid() throws SQLException {
		return commUserMapper.selectLastid(0l);
	} 

}
