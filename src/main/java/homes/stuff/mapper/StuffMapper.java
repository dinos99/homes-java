package homes.stuff.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.comm.vo.CommonMap;
import homes.owner.vo.OwnerVo;
import homes.stuff.vo.StuffVo;

@Mapper
public interface StuffMapper {
	public List<CommonMap> selectBrkComplexList(StuffVo paramVo) ; 
	public List<CommonMap> selectBrkBlockList(StuffVo paramVo) ; 
	public List<CommonMap> selectFloorRoomList(StuffVo paramVo) ;
	public List<CommonMap> selectBrkStuff(StuffVo paramVo) ;
	public List<CommonMap> selectStuffOwnerList(StuffVo paramVo) ; 
	public List<CommonMap> selectRelatedbunji(StuffVo paramVo) ; /* 관련지번 조회 */ 
	
	public List<CommonMap> selectBuldStructinfo(StuffVo paramVo) ; /* 건물 층호실 구조조회 */
	
	public CommonMap selectLedgrinfo(StuffVo paramVo) ; /* 건물(동) 정보조회 */  
	
	public CommonMap selectFloorCount(StuffVo paramVo) throws SQLException ;
	public CommonMap selectPostcodeBuld(StuffVo paramVo) ; 
	public CommonMap selectBuldGroup(StuffVo paramVo) ; /* 집합건물 정보조회 */ 
	public CommonMap selectTitleLedgrinfo(StuffVo paramVo) ; /* 통합DB(표제부) 등록정보 확인 */
	public CommonMap selectHomesBuldinfo(StuffVo paramVo) ;  /* 홈즈DB(개별DB) 등록정보 확인 */
	public CommonMap selectStuffPPscd( String ppscd ) ; 
	public CommonMap isExistsOwner(StuffVo paramVo) ;
	public CommonMap selectMasterbunji( StuffVo paramVo ) ; /* 대표지번 조회 */ 
	
	public CommonMap selectBrkStuffBuld( StuffVo paramVo ) ; 
	public List<CommonMap> selectStuffBuldinfo( StuffVo paramVo ) ; 
	
	public RecapLedgrVo selectHbdRegstrMaster(StuffVo paramVo) ; /* 홈즈_관리대장_총괄표제부 조회 */
	public List<TitleLedgrVo> selectHbdLedgr(StuffVo paramVo) ;        /* 홈즈_관리대장_표제부 조회 */ 
	
	public String isExistsStuff(StuffVo paramVo) ;
	public String isBuldGroup(StuffVo paramVo) ; /* 집합건물인지 확인한다. */ 
	public String createStuffno(StuffVo paramVo) ; 
	public String createOwno(StuffVo paramVo) ; 
	public String getStuffno(StuffVo paramVo) ; 
	public String getHtbdno(StuffVo paramVo) ; /* 홈즈_관리대장_마스터 Key 채번 */ 
	public String getBuldno(StuffVo paramVo) ; /* 홈즈_관리대장_표제부 Key 채번 */ 

	public int insertHbdStuff(StuffVo paramVo) ;
	public int insertBrkStuffBuld(StuffVo paramVo) ; /* TB_BRK_STUFF_BULD 물건_건물등록 */
	public int insertBrkStuffBdRoom(StuffVo paramVo) ; /* TB_BRK_STUFF_BDROOM 물건_호실등록 */
	public int insertBrkOwner(StuffVo paramVo) ;	
	public int insertHbdRegstrMaster(RecapLedgrVo paramVo) ; /* 홈즈_관리대장_마스터 등록 */
	public int insertHbdLedgr(TitleLedgrVo paramVo) ;        /* 홈즈_관리대장_표제부 등록 */
	public int updateBrkStuffSttus(StuffVo paramVo) ; 
	public int updateBrkOwner(OwnerVo paramVo) ;
	public int deleteBrkStuff(StuffVo paramVo) ; 
	public int deleteBrkOwner(StuffVo paramVo) ; 
	public int getLedgrCount(StuffVo paramVo) ; /* 홈즈_관리대장_표제부_카운트 */ 
	public int getHbdTitleLedgrCount(StuffVo paramVo) ; /* 홈즈건물_표제부 등록여부 확인 */	
	public int selectRelatedJibunCount(StuffVo paramVo) ; /* 관련지번 포함여부 */

	public int getStuffCount( StuffVo paramVo ) ; 
	public int getStuffBuldCount( StuffVo paramVo ) ; 
	public int getStuffBdRoomCount( StuffVo paramVo ) ; 
	public int insertBrkBuldStuff( StuffVo paramVo ) ; 
	public int updateBrkBuldStuff( StuffVo paramVo ) ; 
}
