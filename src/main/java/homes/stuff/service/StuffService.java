package homes.stuff.service;

import java.util.List;

import homes.api.buld.vo.BaseOutlineVo;
import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.comm.vo.CommonMap;
import homes.stuff.vo.StuffListVo;
import homes.stuff.vo.StuffVo;
import jakarta.servlet.http.HttpServletRequest;

public interface StuffService {
	public CommonMap selectBrkStuffList(HttpServletRequest request, StuffVo paramVo) ;
	public CommonMap selectFloorRoomInfo( StuffVo paramVo ) ; 
	
	public List<CommonMap> selectBrkStuff( StuffVo paramVo ) ; 	
	public List<CommonMap> selectBrkBlockList( StuffVo paramVo ) ; 	
	public List<CommonMap> selectStuffOwnerList(StuffVo paramVo) ;

	public List<CommonMap> insertStuff(StuffVo paramVo) ;  /* 중개사 물건등록 */ 
	public List<CommonMap> deleteOwner(StuffVo paramVo) ;  /* 중개사 물건삭제 */
	public List<TitleLedgrVo> selectHbdLedgr(StuffVo paramVo) ; /* 홈즈_관리대장_표제부 조회 */
	
	public int updateBrkSttuf( Long brkno, StuffListVo paramVo ) ;
	
	public CommonMap selectPostcodeBuld(StuffVo paramVo) ;
 
	public TitleLedgrVo selectTitleLedgrinfo(StuffVo paramVo) ;   /* 통합DB(표제부) 등록정보 확인 */
	public TitleLedgrVo getApiTitleLedgrinfo( StuffVo paramVo ) ; /* 건축물대장 API 표제부 조회 */

	public BaseOutlineVo getApiBaseOutlineinfo( StuffVo paramVo ) ; /* 건축물대장 API 기본개요 조회 */
	
	/* 홈즈_건물마스터 생성 및 조회 */
	@Deprecated
	public CommonMap manageHomesBuldLedgr( StuffVo paramVo ) ; 
	public TitleLedgrVo manageHomesTitleLedgr( StuffVo paramVo ) ; 
	public RecapLedgrVo getHomesBuldMaster( StuffVo paramVo ) ;
	
}
