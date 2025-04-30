package homes.popup.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommonMap;
import homes.popup.vo.PopCommReqVo;
import homes.popup.vo.PopCommResVo;

public interface PopupService {

	
	/* 공통팝업-담당자검색  */
	public int selectChernmCount(PopCommReqVo paramVo) throws SQLException;
	public List<CommonMap> selectChernmList(PopCommReqVo paramVo) throws SQLException ;
	public PopCommResVo selectChernm(PopCommReqVo paramVo) throws SQLException ;
	
	
}
