package homes.api.buld.service;

import homes.api.buld.vo.BaseOutlineVo;
import homes.api.buld.vo.BuldApiReqVo;
import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.comm.vo.CommonMap;

public interface BuldApiService {

	/* 기본개요 조회 */ 
	public BaseOutlineVo getBaseOutlineinfo(String operation, BuldApiReqVo reqVo) ;
	/* 표제부 조회 */ 
	public TitleLedgrVo getTitleLedgrinfo(String operation, BuldApiReqVo reqVo) ;  
	public CommonMap getTitleLedgrList(String operation, BuldApiReqVo reqVo) ;  
	
	/* 총괄표제부 조회 */ 
	public RecapLedgrVo getRecapLedgrinfo(String operation, BuldApiReqVo reqVo ) ; 
	
}
