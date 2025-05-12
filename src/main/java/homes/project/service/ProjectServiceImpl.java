package homes.project.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.project.vo.ProjectReqVo;
import homes.security.mapper.CommUserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class ProjectServiceImpl implements ProjectService {
	public Logger Log = LogManager.getLogger(ProjectServiceImpl.class) ;
	
	private final CommUserMapper commUserMapper ;
	
	@Override
	@Transactional( readOnly = true )
	public CommResponseVo homesuserList(ProjectReqVo paramVo) throws SQLException {
		Log.info("*** pageno: {}", paramVo.getPgno()) ;
		paramVo.setPage(); 
		Long t_cnt = commUserMapper.selectHomesUserCount(paramVo) ;
		List<CommonMap> dataList = commUserMapper.selectHomesUser(paramVo) ;
		return new CommResponseVo(t_cnt, paramVo.getPgno(), null, dataList) ;
	}

}
