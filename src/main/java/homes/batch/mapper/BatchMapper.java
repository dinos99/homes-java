package homes.batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.vo.CommonMap;
import homes.data.vo.BaseOutLineVo;
import homes.data.vo.BaseSummaryVo;
import homes.data.vo.LedgrMstrVo;
import homes.data.vo.LedgrVo;

@Mapper
public interface BatchMapper {
	/* 건축물관리대장 - 기본개요(BLD001) 등록/삭제 */ 
	public int deleteBaseSummry( String arcode ) ; 
	public int insertBaseSummry( CommonMap pMap ) ; 
	
	
	/* 작업등록 및 진행상태 변경 */ 
	public int insertBatchjob( CommonMap pMap ) ; 
	public int updateBatchjob( CommonMap pMap ) ;
	public int insertBatchLog( BatchVo batchVo ) ; 
	public int updateBatchLog( BatchVo batchVo ) ; 

	public Long selectBatchJobListCount(BatchReqVo reqMap) ;
	public List<CommonMap> selectBatchJobList(BatchReqVo reqMap) ;
	
	/* 표제부 법정동코드 NULL변환 */
	public List<CommonMap> selectNullLegcdList() ; 
	public List<CommonMap> selectConvArcodeList(String arcd) ;
	int updateLegcdTotalLedger( CommonMap pMap ) ;
	int deleteLegcdTotalLedger( String arcd ) ;
	
	/* 실사용 부분 */ 
	public int updateLedgrHtbdno( CommonMap pMap ) ; /* 홈즈_관리대장_표제부 마스터 PK update */ 
	public List<CommonMap> selectRelatedMaster( CommonMap pMap ) ; /* 홈즈_관리대장_마스터의 대표지번 조회 */
	
	/* 홈즈 건물관리대장 정보조회 및 변경 */ 
	public int getHBT001Count( BatchVo batchVo ) ; 
	public int getHbdBaseSummryCount( BaseOutLineVo paramVo ) ; 
	public int getHbdLedgrMstrCount( LedgrMstrVo paramVo ) ;
	public int getHbdLedgrCount( LedgrVo paramVo ) ;
	
	public String getHomesHtbdno( BaseOutLineVo paramVo ) ; /* 홈즈_관리대장_마스터_PK 채번 */ 
	public String getHomesBuldno( BaseOutLineVo paramVo ) ; /* 홈즈_관리대장_PK 채번 */ 

	public List<BaseOutLineVo> selectBatchBaseOutLine( BaseOutLineVo paramVo ) ; /* 홈즈_건물대장_기본개요 등록대상조회 */ 
	public List<BaseOutLineVo> selectBaseOutLine( BaseOutLineVo paramVo ) ; /* 단지(집합건물 등록대상 조회 */
	public LedgrMstrVo selectBaseLedgrMstr( String totalRegstrPk) ; /* 홈즈_관리대장_마스터 등록조회 */ 

	public int insertBaseSummry( BaseSummaryVo paramVo ) ;  /* 홈즈_건물대장_기본개요 등록 */
	public int updateBaseOutLine( BaseOutLineVo paramVo ) ; /* 홈즈_건물대장_기본개요 배치처리 */
	public int insertLedgrMstr( LedgrMstrVo paramVo ) ;     /* 홈즈_건물대장_마스터 등록 */ 
	public int insertLedgr( LedgrVo paramVo ) ;             /* 홈즈_관리대장_표제부 등록 */

}
