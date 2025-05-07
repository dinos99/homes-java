package homes.security.vo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommUserVo {
	/* 사용자 번호 */ 
	private long userno ;
	
	/* 사용자 이메일 */
	private String email ;
	
	/* 사용자 명 */ 
	private String usernm ;
	
	/* 사용자 업무 권한 */ 
	private String userRole ;
	
	/* 사용자 상태코드 */
	private String userSttus ; 
	
	/* 최초 가입일시 */ 
	private Date frstSbscrbDt ;
	
	/* 최종 로그인 일시 */ 
	private Date lastLoginDt ; 
	
	/* 비밀번호 변경일시 */ 
	private Date passwordChgDt ;
	
	/* 로그인 실패 회수 */ 
	private int loginFailrCo ; 
	
	/* 로그인 유형 [ LGT000: 직접로그인, LGT001: KAKAO, LGT002: NAVER ] */ 
	private String loginTy ; 
	
	/* 로그인 API 연계 ID */
	private String ifUserId ;
	
	private String accessToken ; 
	
	private String arcode ; 
	
	private String mbleCttpc ;
	
}
