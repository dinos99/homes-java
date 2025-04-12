package homes.security.vo;

import lombok.Data;

@Data
public class LoginRequestVo {
	/* 사용자 번호 */ 
	private String userNo ;
	
	/* 사용자 이메일 */ 
    private String email;
    
    /* 사용자 비밀번호 */ 
    private String password;
    
}
