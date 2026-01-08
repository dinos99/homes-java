package homes.buldapi.service;

import java.util.List;

import homes.buldapi.vo.BuldHubReqVo;
import homes.buldapi.vo.BuldHubResVo;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;

public interface BuldHubService {
	
	/* 기본개요 등록 */ 
	public BuldHubResVo insertHbdSummary( BuldHubReqVo pMap ) ; 
	/* 표제부 등록 */  
	public BuldHubResVo insertHbdLedgr( BuldHubReqVo paramVo ) ;
	/* 마스터 등록 */  
	public BuldHubResVo insertHdbLedgrMst( BuldHubReqVo paramVo ) ;
	
	/* 층별개요(임시) 등록 */  
	public BuldHubResVo insertTempLedgrFloor( BuldHubReqVo paramVo ) ;
	/* 층별개요 등록 */  
	public BuldHubResVo insertLedgrFloor( BuldHubReqVo paramVo ) ;
	
	/* 층별개요 등록 */  
	public BuldHubResVo insertLedgrPssion( BuldHubReqVo paramVo ) ;
	
	public CommResponseVo getRdRelateJibun( BuldHubReqVo paramVo )  ; 
	public CommResponseVo getBaseOutLine( BuldHubReqVo paramVo ) ; 

	/* 건축물대장 HUB API - 표제부 조회 */ 
	public CommResponseVo getLedgrinfo( BuldHubReqVo paramVo );
	/* 건축물대장 HUB API - 총괄표제부 조회 */  
	public CommResponseVo getRecapTitleinfo( BuldHubReqVo paramVo );
	/* 건축물대장 HUB API - 층별개요 조회 */  
	public CommResponseVo getFloorOutLine( BuldHubReqVo paramVo );
	/* 건축물대장 HUB API - 전유부 조회 */  
	public CommResponseVo getBrExposInfo( BuldHubReqVo paramVo );
	/* 건축물대장 HUB API - 전유공용면적 조회 */  
	public CommResponseVo getBrExposPubuseAreaInfo( BuldHubReqVo paramVo );
	
	public List<CommonMap> selectBuldList ( BuldHubReqVo paramVo ) ; 
	public List<CommonMap> selectDongList ( BuldHubReqVo paramVo ) ; 
}
