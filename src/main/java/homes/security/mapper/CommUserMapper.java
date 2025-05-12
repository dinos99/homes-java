package homes.security.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;
import homes.project.vo.ProjectReqVo;
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
	
	/* 공통사용자 신규등록 */
	public int insertCommuser( CommUserReqVo vo ) ;
	
	public Long getLastUserno( CommUserReqVo vo ) ; 
	
	public long selectLastid( long l ) ; 

	/* 이메일 중복확인 */
	public String isExistUserByEmail( String email ) ;
	
	/* 핸드폰 중복확인 */
	public String isExistUserByCttpc( String mblecttpc ) ;
	
	public Long selectHomesUserCount( ProjectReqVo paramVo) throws SQLException ; 
	public List<CommonMap> selectHomesUser( ProjectReqVo paramVo) throws SQLException ; 
	
}
