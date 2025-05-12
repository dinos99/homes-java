package homes.manager.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerVo {

	private Long   managerno ;
	private String managernm ; 
	private String empno ;
	private String email ;
	private String mngrsttus ; /* 관리자 상태 */ 
	private String mngrrole ; /* 관리자 역할 */ 
	
}
