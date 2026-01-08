package homes.api.buld.vo;

import org.springframework.core.env.Environment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class BuldApiReqVo {

	private Environment env;
	
	/* service key */
	@Getter(AccessLevel.NONE)
	private final String _API_DEV_SERVICE_KEY = "c1545ed27e1bbfcdd6d3672e119954a3d19b7b4062b2654bf1b97b14015af495" ;
	
	/* API URL */ 
	@Getter(AccessLevel.NONE)
	private final String _API_DEV_BASE_URL = "https://apis.data.go.kr/1613000/BldRgstHubService" ; 
	
	/* 표제부 조회 */ 
	private String arcd ;     /* 필수: sigunguCd */
	private String legcd ;    /* 필수: bjdongCd  */
	private String platGbCd ; /* platGbCd        */
	private String bunjib ;   /* bun             */
	private String bunjij ;   /* ji              */
	private String stde ;     /* startDate       */
	private String edde ;     /* endDate         */
	
	/* return type: _type=json, _type=xml */
	private String rtype ;
	
	private int numrows = 20;     /* numOfRows       */
	private int page    = 1;      /* pageNo          */
	
	/* 내부검색용 파라미터 */ 
	private String buldnm ;   /* 건물명          */
	private String bregstrPk; /* 표제부 PK       */
	private String tregstrPk; /* 총괄표제부 PK   */
	
	public BuldApiReqVo(Environment enviroment) {
		this.env = enviroment ; 
		this.numrows = 20 ; 
		this.page = 1 ; 
	}
	public BuldApiReqVo(Environment enviroment, String arcd, String legcd, String bun, String ji, int nrow) {
		this.env = enviroment ; 
		this.arcd = arcd ; 
		this.legcd = legcd ;
		this.bunjib = bun ; 
		this.bunjij = ji ; 
		this.numrows = nrow ;  
		this.page = 1 ; 
	}
	
	public String getServiceKey() {
		String profile = env.getActiveProfiles()[0] ;
		if ( "LOCAL".equals(profile)) {
			return this._API_DEV_SERVICE_KEY ; 
		} else if ( "DEV".equals(profile)) {
			return this._API_DEV_SERVICE_KEY ; 
		} else if ( "TEST".equals(profile)) {
			return this._API_DEV_SERVICE_KEY ; 
		}
		return "" ; 
	}
	
	public String getApiBaseUrl() {
		String profile = env.getActiveProfiles()[0] ;
		if ( "LOCAL".equals(profile)) {
			return this._API_DEV_BASE_URL ; 
		} else if ( "DEV".equals(profile)) {
			return this._API_DEV_BASE_URL ; 
		} else if ( "TEST".equals(profile)) {
			return this._API_DEV_BASE_URL ; 
		}
		return "" ; 
	}
}
