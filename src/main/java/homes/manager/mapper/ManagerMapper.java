package homes.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.manager.vo.ManagerVo;
import homes.manager.vo.TodoVo;

@Mapper
public interface ManagerMapper {

	public int getWkidCount( TodoVo paramVo ) ;
	public int insertTodoList( TodoVo paramVo ) ; 
	public int updateTodoList( TodoVo paramVo ) ; 
	public int insertManager( ManagerVo paramVo ) ;
	
	public String getEmpno( String empTy ) ; 
	
	public List<TodoVo> selectTodoList( TodoVo paramVo ) ;
	
}
