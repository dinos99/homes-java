package homes.api.naver.vo;

import lombok.Data;

@Data
public class NaverClientVo {
	private final String id = "UzyPZe2RD_eiUNo0ArnT" ;
	private final String secret = "RC9vNkO5pc" ; 
	private final String naverLocal = "https://openapi.naver.com/v1/search/local.json" ;
	private final String apiuri = "https://openapi.naver.com/v1/search/local.json" ;
	public String query ;
	public String sort ; 
	public int display;
	public int start;
}
