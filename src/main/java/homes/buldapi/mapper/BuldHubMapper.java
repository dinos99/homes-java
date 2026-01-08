package homes.buldapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.buldapi.vo.BuldHubReqVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface BuldHubMapper {
	
//	public int selectLedgrCount( String buldRegstrPk ) ; 
	public int selectTempLedgrCount( String buldRegstrPk ) ; 
	public int selectTempLedgrMstrCount( String totalRegstrPk ) ; 
	public int selectHbdLedgrMstrCount( String htbdno ) ; 
	
	public int selectHBDLedgrCount( BuldHubReqVo paramVo ) ;
	public int selectHBDLedgrMasterCount( BuldHubReqVo paramVo ) ;
	
	public int insertHbdSummary( BuldHubReqVo paramVo ) ;
	public int insertHbdLedgr( BuldHubReqVo paramVo ) ; 
	public int insertHbdLedgrMstr( BuldHubReqVo paramVo ) ; 
	public int insertLedgrFloor( BuldHubReqVo paramVo ) ; 
	public int insertLedgrPssion( BuldHubReqVo paramVo ) ; 
	
	public int insertTempLedgr( CommonMap pMap ) ; 
	public int insertTempLedgrMstr( CommonMap pMap ) ; 
	public int insertTempLedgrFloor( CommonMap pMap ) ; 
	
	public int updateHbdLedgrMaster( CommonMap pMap ) ; 
	
	public Long selectRdRelateJibunCount( BuldHubReqVo paramVo ) ; 
	public Long selectBaseOutLineCount( BuldHubReqVo paramVo ) ; 
	
	public List<CommonMap> selectBuldList( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectDongList( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectRdRelateJibun( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectBaseOutLine( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectMasterSource( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectMasterUpdateList( BuldHubReqVo paramVo ) ;
	
	public List<CommonMap> selectHBDLedgrList( BuldHubReqVo paramVo ) ;
	public List<CommonMap> selectHBDLedgrMasterList( BuldHubReqVo paramVo ) ;
	
	
}
