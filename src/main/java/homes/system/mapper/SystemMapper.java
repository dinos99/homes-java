package homes.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.system.vo.BuldRegstrReqVo;
import homes.system.vo.DomainVo;
import homes.system.vo.SystemReqVo;

@Mapper
public interface SystemMapper {
	Long selectDomainListCount(SystemReqVo reqVo) ;
	List<CommonMap> selectDomainList(SystemReqVo reqMap) ;
	CommonMap selectExistsDomain( DomainVo domainVo ) ;
	
	int modifyDomain( DomainVo paramVo ) ;
	int insertDomain( DomainVo paramVo ) ;
	
	/* 총괄표제부 조회 */ 
	Long selectTotalLedgrCount( BuldRegstrReqVo paramVo ) ; 
	List<CommonMap> selectTotalLedgr( BuldRegstrReqVo paramVo ) ; 
}
