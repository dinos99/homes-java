package homes.api.buld.vo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TitleLedgrVo {
	public TitleLedgrVo() {
		this.htbdno    = ""  ; 
		this.buldno    = ""  ; 
		this.bregstrPk = "0" ; 
		this.tregstrPk = "0" ; 
		this.delYn     = "N" ; /* 삭제여부 (기본 N) */
		this.batchYn   = "N" ; /* 배치여부 (기본 N) */
		this.parkngco  = 0 ;   /* 표제부는 주차대수 없음 */ 

		/* 건축물관리대장 API 페이징정보 */ 
		this.page    = 0 ; 
		this.numrows = 0 ; 
		this.tcnt    = 0 ;
		this.rnum    = 0 ;
	}
	
	private int flgroundco ;   /* 지상층수 */
	private int flunderco ;    /* 지하층수 */ 
	private int rideElvtrco ;  /* 승용_엘리베이터_수 */
	private int emgrElvtrco ;  /* 비상_엘리베이터_수 */
	private int hshldco ;      /* 세대_수(세대) */
	private int fmlyco ;       /* 가구_수(가구) */
	private int roomco ;       /* 호실_수(호)   */ 
	private int parkngco ;     /* 주차_수 */
	
	private int inco ;         /* 조회건수 (0인경우 API조회) */
	
	private String htbdno ;    /* 홈즈_관리대장_마스터_PK */
	private String buldno ;    /* 홈즈_관리대장_표제부_PK */ 
	private String bregstrPk ; /* 관리_건축물대장_표제부_PK     */
	private String tregstrPk ; /* 관리_건축물대장_총괄표제부_PK */
	private String buldgb ;    /* 건축물 구분: [1]일반건물, [2]집합건물 */
	private String buldnm ;    /* 건물명 */
	private String bdaddr ;    /* 건물주소(지번) */
	private String rdaddr ;    /* 건물주소(도로명) */
	private String arcd ;      /* 지역코드_행정동 코드(시도/시군구) */
	private String legcd ;     /* 지역코드_법정동 코드 */
	private String bunjib ;    /* 지번_번 */
	private String bunjij ;    /* 지번_지 */ 
	private String blocknm ;    
	private String dongnm ;    /* 건물_동_명 */ 
	private String ppscd ;     /* 건물_용도_코드 */
	private String ppsnm ;     /* 건물_용도_명 */
	private String ppsetcnm ;  /* 건물_용도_기타_명 */
	private String cfmvgb ;    /* 승인_입주_구분(0: 미선택, 1: 승인일자, 2: 입주일자) */
	private String platgb ;    /* 대지_구분_코드 */ 
	private String confde ;    /* 승인_일자 */
	private String moveinde ;  /* 입주(예정)_일자 */	
	private String crde ;      /* 생성_일자 */
	private String delYn ;     /* 삭제_여부 */
	private String useYn ;     /* 사용_여부 */ 
	private String batchYn ;   /* 배치_여부 */ 

	private String rdcode ; 
	private String rdlegcd ; 
	private String rdMainBun ; 
	private String rdSubBun ;
	private String underAt ; 
	
	private Float supplyAr ; /* 공급_면적 */
	private Float platAr  ;  /* 대지_면적 */ 
	private Float buldAr  ;  /* 건물_면적 */
	private Float totalAr ;  /* 총_면적   */
	
	
	/* 관련지번 및 대표지번 */
	private String arname ; 
	private String mstrbun ;
	
	/* 건축물관리대장 API 페이징정보 */ 
	private int rnum    = 0 ;
	private int page    = 0 ; 
	private int numrows = 0 ; 
	private int tcnt    = 0; 
	
	private Long userno ; 
	private Long brkno ;
	
}
