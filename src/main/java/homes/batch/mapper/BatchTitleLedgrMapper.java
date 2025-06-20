package homes.batch.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;

@Mapper
public interface BatchTitleLedgrMapper {
	
	/* 표제부 관련테이블 TRUNCATE */ 
	public int truncateTitleLedger() ; 
	public int truncateTitleLedgrAblty() ; 
	public int truncateTitleLedgrAr() ; 
	public int truncateTitleLedgrPps() ; 
	public int truncateTitleLedgrPrmissDe() ; 
	public int truncateTitleLedgrUnitCo() ;
	
	/* 표제부 관련테이블 insert */
	public int insertBdTitleLedgr(CommonMap pMap) ;
	public int insertBdTitleLedgrAblty(CommonMap pMap) ;
	public int insertBdTitleLedgrAr(CommonMap pMap) ;
	public int insertBdTitleLedgrpps(CommonMap pMap) ;
	public int insertBdTitleLedgrde(CommonMap pMap) ;
	public int insertBdTitleLedgrco(CommonMap pMap) ;
	
}
