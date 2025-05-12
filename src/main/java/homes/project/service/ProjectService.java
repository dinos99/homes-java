package homes.project.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.project.vo.ProjectReqVo;

public interface ProjectService {

	public CommResponseVo homesuserList(ProjectReqVo paramVo) throws SQLException ;
	
}
