package homes.comm.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.comm.vo.FileVo;

@Mapper
public interface CommonMapper {
	public CommonMap selectArCode(String areacode ) throws SQLException ; 
	public CommonMap selectSidoCode(String sdcode ) throws SQLException ; 
	
	public List<CommonMap> selectSidoList() ; 
	public List<CommonMap> selectSggList(String sdcode) throws SQLException; 
	public List<CommonMap> selectEmdList(String arcode) throws SQLException; 
	
	public int addfile(FileVo fileVo) throws SQLException ; 
	public Long getLastfileno(long userno) throws SQLException ; 
	
	public List<CommonMap> getCommCodeList(String upcd) throws SQLException;
}
