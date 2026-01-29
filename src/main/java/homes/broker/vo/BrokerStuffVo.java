package homes.broker.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrokerStuffVo {
	
	int officeno ;   /* 중개사무소 번호 */ 
	int brkno ;      /* 중개사     번호 */
	
	String stuffno ; /* 물건번호 */  
	
	String arcd ; 
	String legcd ; 
	String bunjib ; 
	String bunjij ;
	String buldgb ; 
	String ppscd ; 
	String hppscd ; 
	String sfsttus ; 
	String htbdno ; 
	String hbdno ; 
	String hpsno ; 
	
}
