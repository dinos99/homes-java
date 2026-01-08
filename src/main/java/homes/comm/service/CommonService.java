package homes.comm.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;

public interface CommonService {
	public CommonMap selectArCode(String areacode ) throws SQLException ;

	public List<CommonMap> selectSidoList() ; 
	public List<CommonMap> selectSggList(String sdcode) throws SQLException; 
	public List<CommonMap> selectEmdList(String arcode) throws SQLException; 
		
	/* 공통사용자 신규등록 */
	public Long insertCommuser( CommUserReqVo vo ) throws SQLException ;
	
	public long selectLastid() throws SQLException ; 
	
}
