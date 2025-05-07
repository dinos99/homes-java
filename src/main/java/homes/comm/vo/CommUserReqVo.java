package homes.comm.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommUserReqVo {
	 
	private int loginFailrCo ; 
	private long userno ;
	
	private String email ; 
	private String password ; 
	private String userNm ; 
	private String userRole ; 
	private String userSttus ; 
	private String birth ; 
	private String gndr ; 
	private String ccbCd ; 
	private String mbleCttpc ; 
	private String mbleCttpc01 ; 
	private String mbleCttpc02 ; 
	private String mbleCttpc03 ; 
	private String mbleCertno ;
	private String loginTy ; 
	private String agreYn ; 

	private String brokerty ; /* 중개사만 해당 [BRK001]: 대표, [BRK002]: 소속중개사 */  
}
