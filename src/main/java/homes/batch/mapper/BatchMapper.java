package homes.batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.batch.vo.BatchReqVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BatchMapper {
	/* 건축물관리대장 - 기본개요(BLD001) 등록/삭제 */ 
	public int deleteBaseSummry( String arcode ) ; 
	public int insertBaseSummry( CommonMap pMap ) ; 
	
	/* 건축물관리대장 - 총괄표제부 등록/삭제 */ 
	public int deleteTotalTitleLedgr( String pk ) ;
	public int insertTotalTitleLedgr( CommonMap pMap ) ;

	/* 건축물관리대장 - 표제부 등록/삭제 */ 
	public int deleteTitleLedgr( String pk ) ;
	public int insertTitleLedgr( CommonMap pMap ) ;
	
	/* 건축물관리대장 -전유부 등록/삭제 */
	public int deletePssionLedgr( String pk ) ; 
	public int insertPssionLedgr( CommonMap pMap ) ; 

	/* 단지정보 변경건 조회 */ 
	public String selectComplexUpdated( String chngde ) ;
	/* 단지정보 삭제(전체데이터삭제) */ 
	public int deleteComplex( CommonMap pMap ) ;
	/* 변경건으로부터 단지정보 등록 */ 
	public int insertComplex( CommonMap pMap ) ; 
	/* 단지 그룹정보 조회 */ 
	public CommonMap selectComplexCpxgno( CommonMap pMap ) ; 
	/* 단지 입력대상 조회 */
	public List<CommonMap> selectComplexList( CommonMap pMap ) ;
	
	/* 작업등록 및 진행상태 변경 */ 
	public int insertBatchjob( CommonMap pMap ) ; 
	public int updateBatchjob( CommonMap pMap ) ;

	public Long selectBatchJobListCount(BatchReqVo reqMap) ;
	public List<CommonMap> selectBatchJobList(BatchReqVo reqMap) ;
	
	public CommonMap selectBatchTagetFile ( String jobid ) ;
	
	public List<CommonMap> selectSidocodeList( String sdcode ) ;  
	public List<CommonMap> selectSggcodeList( String sdcode ) ;  

	/* 기본개요 원시데이터 등록/삭제 */
	public int deleteBaseSummryRawdata( String uuid ) ; 
	public int insertBaseSummryRawData( CommonMap pMap ) ; 
}
