package homes.comm.service;

import homes.comm.vo.CommCodeListVo;

public interface CommCodeService {

	public CommCodeListVo getCodeAllList(String grpcd) ; 
	public CommCodeListVo getCodeGroupList(String grpcd) ;
	
	/* 부동산구분 코드목록조회 ( GRP_CD = 'EST' )  */ 
	public CommCodeListVo getEstGroupList() ; 
}
