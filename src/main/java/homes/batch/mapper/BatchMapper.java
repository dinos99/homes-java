package homes.batch.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;

@Mapper
public interface BatchMapper {
	public int deleteSummryTitlledgr( String pk ) ;
	public int insertSummryTitlledgr( CommonMap pMap ) ;
	public int insertBatchjob( CommonMap pMap ) ; 
	public int updateBatchjob( CommonMap pMap ) ; 
}
