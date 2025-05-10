package homes.comm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommCodeVo;

@Mapper
public interface CommCodeMapper {

	/* 모든 공통코드조회*/
	public List<CommCodeVo> getCodeAllList(CommCodeVo cmap) ; 
	/* GRP_CD로 공통코드조회 */
	public List<CommCodeVo> getCodeGroupList(CommCodeVo cmap) ; 
	
}
