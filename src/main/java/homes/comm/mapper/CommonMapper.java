package homes.comm.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;
import homes.comm.vo.FileVo;

@Mapper
public interface CommonMapper {

	public List<CommonMap> selectSidoList(CommReqVo paramVo) throws SQLException; 
	public List<CommonMap> selectSggList(CommReqVo paramVo) throws SQLException; 
	public List<CommonMap> selectEmdList(CommReqVo paramVo) throws SQLException; 
	
	public int addfile(FileVo fileVo) throws SQLException ; 
	public Long getLastfileno(long userno) throws SQLException ; 
	
}
