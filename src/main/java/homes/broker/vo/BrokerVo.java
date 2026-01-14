package homes.broker.vo;

import lombok.Data;

@Data
public class BrokerVo {
	
	public BrokerVo() {
		this.officeno = 1 ; 
		this.brokerno = 17 ; 
		this.officenm = "금성중개사무소" ; 
		this.brokernm = "중개인01" ; 
		this.arcode   = "1171011100" ; /* 서울시 송파구 방이동 */ 
		this.arcd     = "11710" ;      /* 서울시 송파구 */
		this.legcd    = "11100" ;      /* 방이동 */ 
	}

	private int  officeno ; 
	private int  brokerno ; 
	private String brokernm ; 
	private String officenm ; 
	private String brokerty ; 
	private String brokersttus ;
	
	private String arcode ; 
	private String arcd ; 
	private String legcd ; 
	
	private Float latX ; 
	private Float lngY ; 
}
