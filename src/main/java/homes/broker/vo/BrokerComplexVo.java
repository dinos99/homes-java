package homes.broker.vo;

import lombok.Data;

@Data
public class BrokerComplexVo {
	Long   brkno ; 
	String cplxno ; 
	String cplxTy ;
	String arcd ; 
	String legcd ; 
	String buldno ; 
	
	String x ; /* 건물 X좌표 */ 
	String y ; /* 건물 Y좌표 */ 
}
