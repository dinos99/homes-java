package homes.comm.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AccessTokenVo {
	
	private long userNo ; 
	private String userNm ;
	private String email ; 
	private String role ; 
	
	/* 토큰 발급일시 */ 
	private Date issuedAt ;
	
	/* 토큰 만료일시 */
	private Date expiration ;
	
	private String accessToken ; 
	
}
