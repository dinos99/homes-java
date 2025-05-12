package homes.manager.service;

import java.sql.SQLException;

import homes.manager.vo.ManagerVo;

public interface ManagerService {

	public ManagerVo registmanager( ManagerVo paramVo) throws  SQLException ;
}
