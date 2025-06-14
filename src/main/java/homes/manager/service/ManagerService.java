package homes.manager.service;

import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import homes.manager.vo.ManagerVo;

public interface ManagerService {

	public ManagerVo registmanager( ManagerVo paramVo) throws  SQLException ;
	public int  uploadBuildFile( String fileTyype, MultipartFile file)  ;
}
