package homes.buldapi.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseOutLineVo {
	private Long mngrno ;
	private String htbdno ; 
	private String hbdno ; 
	
	private String totalRegstrPk ;
	private String buldRegstrPk ; 
	private String buldgb ; 
	private String arcd ; 
	private String legcd ; 
	private String bunjib ; 
	private String bunjij ; 
	private String rdcode ;
	private String rdlegcd ; 
	private String underAt ; 
	private String rdMainBun ; 
	private String rdSubBun ; 
	private String delYn ; 
	private String batchAt ;
	private String batchde ; 
	private String crde ; 
	private String cplxAt ;
	private String isComplex ; 
	
	private String bfBatchAt ; 
	private String afBatchAt ; 
	
}
