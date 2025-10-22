package homes.stuff.service;

import java.util.List;

import homes.comm.vo.CommonMap;
import homes.stuff.vo.StuffVo;
import jakarta.servlet.http.HttpServletRequest;

public interface StuffService {
	public CommonMap selectBrkStuffList(HttpServletRequest request, StuffVo paramVo) ;
	public CommonMap selectFloorRoomInfo( StuffVo paramVo ) ; 
	
	public List<CommonMap> selectBrkBlockList( StuffVo paramVo ) ; 	
	public List<CommonMap> selectStuffOwnerList(StuffVo paramVo) ;

	public List<CommonMap> insertStuff(StuffVo paramVo) ;  /* 중개사 물건등록 */ 
	public List<CommonMap> deleteOwner(StuffVo paramVo) ;  /* 중개사 물건삭제 */ 
}
