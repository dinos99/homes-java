package homes.stuff.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.stuff.vo.StuffVo;

@Mapper
public interface StuffMapper {
	public List<CommonMap> selectBrkComplexList(StuffVo paramVo) throws SQLException ; 
	public List<CommonMap> selectBrkBlockList(StuffVo paramVo) throws SQLException ; 
	public List<CommonMap> selectFloorRoomList(StuffVo paramVo) throws SQLException ; 
	
	public CommonMap selectFloorCount(StuffVo paramVo) throws SQLException ;
	
	public List<CommonMap> selectStuffOwnerList(StuffVo paramVo) ; 
	
	public String isExistsStuff(StuffVo paramVo) ; 
	public String createStuffno(StuffVo paramVo) ; 
	public String createOwno(StuffVo paramVo) ; 
	public String getStuffno(StuffVo paramVo) ; 

	public CommonMap isExistsOwner(StuffVo paramVo) ; 
	
	public int insertHbdStuff(StuffVo paramVo) ;
	public int insertBrkStuff(StuffVo paramVo) ;
	public int insertBrkOwner(StuffVo paramVo) ;
	
	public int deleteBrkStuff(StuffVo paramVo) ; 
	public int deleteBrkOwner(StuffVo paramVo) ; 
}
