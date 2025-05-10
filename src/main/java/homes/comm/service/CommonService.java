package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;

public interface CommonService {

	public List<CommonMap> selectSidoList(CommReqVo paramVo) throws SQLException;
	public List<CommonMap> selectSggList(CommReqVo paramVo) throws SQLException; 
	public List<CommonMap> selectEmdList(CommReqVo paramVo) throws SQLException; 
	
	public CommonMap selectArcodeList(CommReqVo paramVo) throws SQLException; 

	/* 공통사용자 신규등록 */
	public Long insertCommuser( CommUserReqVo vo ) throws SQLException ;
	
	public long selectLastid() throws SQLException ; 
}
