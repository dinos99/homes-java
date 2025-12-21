package homes.api.kakao.vo;

import lombok.Data;

@Data
public class KakaoApiReqVo {
	private final String KAKAO_REST_API_KEY = "121d01d42f129af12afc7e6afd2ef3ef" ;
	
	public final String KAKAO_REST_API_TRANS_COORDS_URL = "https://dapi.kakao.com/v2/local/geo/transcoord.json" ; 
	
	public String query ;
	public String arcd ; 
	public String legcd ; 
	public String bun ; 
	public String ji ;
	public String x; 
	public String y; 
	public String getKakaoApiKey() {
		return this.KAKAO_REST_API_KEY ; 
	}
}
