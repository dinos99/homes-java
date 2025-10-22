package homes.comm.service;

import java.util.List;

import homes.comm.vo.CommCodeListVo;
import homes.comm.vo.CommCodeVo;
import homes.comm.vo.CommonMap;

public interface CommCodeService {

	public CommCodeListVo getCodeAllList(String grpcd) ; 
	public CommCodeListVo getCodeGroupList(String grpcd) ;
	public List<CommonMap> getCommCodeList(CommCodeVo paramVo) ;
	
	/* 부동산구분 코드목록조회 ( GRP_CD = 'EST' )  */ 
	public CommCodeListVo getEstGroupList() ; 
}
