package homes.popup.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.popup.vo.PopCommReqVo;

@Mapper
public interface PopupMapper {

	/* 공통팝업-담당자검색  */
	public int selectChernmCount(PopCommReqVo paramVo) throws SQLException; 
	public List<CommonMap> selectChernmList(PopCommReqVo paramVo) throws SQLException; 
	
}
