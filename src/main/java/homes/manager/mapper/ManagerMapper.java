package homes.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.manager.vo.ManagerVo;

@Mapper
public interface ManagerMapper {
	
	public String getEmpno( String empTy ) ;
	public int insertManager( ManagerVo paramVo ) ; 
	
}
