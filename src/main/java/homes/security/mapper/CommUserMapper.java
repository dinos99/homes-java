package homes.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.security.vo.CommUserVo;
import homes.security.vo.LoginRequestVo;
import homes.security.vo.UpdateLoginInfoReqVo;

@Mapper
public interface CommUserMapper {
	
	/* 이메일로 사용자 정보조회  */
	public CommUserVo findCommUserByEmail(LoginRequestVo vo) ; 
	
	/* 사용자번호로 사용자 정보조회 */ 
	public CommUserVo findCommUserByUserNo(long userNo) ;
	
	public int updateUserLogininfo( UpdateLoginInfoReqVo paramVo) ; 
	
}
