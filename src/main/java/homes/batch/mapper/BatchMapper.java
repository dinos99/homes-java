package homes.batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DuplicateKeyException;

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

	/* 건축물관리대장 -전유부 등록/삭제 */
	public int insertPssionLedgr( CommonMap pMap ) ; 
	
	/* 작업등록 및 진행상태 변경 */ 
	public int insertBatchjob( CommonMap pMap ) ; 
	public int updateBatchjob( CommonMap pMap ) ;

	public Long selectBatchJobListCount(BatchReqVo reqMap) ;
	public List<CommonMap> selectBatchJobList(BatchReqVo reqMap) ;
	
	/* 기본개요 등록/삭제 */
	public int insertHbdBaseSummry( CommonMap pMap ) ; 
	public int insertHbdBuld( CommonMap pMap ) ; 
	public int insertHbdAgBuld( CommonMap pMap ) ; 
	public int insertHbdPssionBuld( CommonMap pMap ) throws DuplicateKeyException ;

	public int updatePssionLedger( CommonMap pMap ) ;
	
	/* 표제부 법정동코드 NULL변환 */
	public List<CommonMap> selectNullLegcdList() ; 
	public List<CommonMap> selectConvArcodeList(String arcd) ;
	int updateLegcdTotalLedger( CommonMap pMap ) ;
	int deleteLegcdTotalLedger( String arcd ) ;
	
}
