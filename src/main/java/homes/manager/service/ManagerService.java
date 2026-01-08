package homes.manager.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import homes.comm.vo.CommonMap;
import homes.manager.vo.ManagerVo;
import homes.manager.vo.TodoVo;

public interface ManagerService {

	public ManagerVo registmanager( ManagerVo paramVo) throws  SQLException ;
	public int  uploadBuildFile( String fileTyype, MultipartFile file)  ;
	
	public List<TodoVo> getTodoList( TodoVo paramVo ) ; 
	
	public CommonMap saveTodoList( Long mngrno,  List<TodoVo> paramVo ) ; 
	public CommonMap updateTodoList( Long mngrno,  TodoVo paramVo ) ; 
	
}
