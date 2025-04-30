package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;

public interface CommonService {

	public List<CommonMap> selectSidoList(CommReqVo paramVo) throws SQLException;
	public List<CommonMap> selectSggList(CommReqVo paramVo) throws SQLException; 
	public List<CommonMap> selectEmdList(CommReqVo paramVo) throws SQLException; 
	
	public CommonMap selectArcodeList(CommReqVo paramVo) throws SQLException; 
	
}
