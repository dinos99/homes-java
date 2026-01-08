package homes.comm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommCodeVo;
import homes.comm.vo.CommonMap;

@Mapper
public interface CommCodeMapper {

	/* 모든 공통코드조회*/
	public List<CommCodeVo> getCodeAllList(CommCodeVo paramVo) ; 
	/* GRP_CD로 공통코드조회 */
	public List<CommCodeVo> getCodeGroupList(CommCodeVo paramVo) ;
	
	/* 여러공통코드 조회 */ 
	public List<CommonMap> getCommCodeList(CommCodeVo paramVo) ; 
	
	/* UPPER_CD로 공통코드조회 */
	public List<CommonMap> getCodeListbyUpcode(String grpcd) ;
	
	/* View를 통하여 부동산 용도코드를 조회한다 */
	public List<CommonMap> getPpsCdList( String ppscd ) ; 
	
	public List<CommonMap> getppsCodeList(String ppscd) ; 
}
