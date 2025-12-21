package homes.data.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgrMstrVo {
	private int hshldco;   /* 세대_수(세대)       */
	private int fmlyco;    /* 가구_수(가구)       */
	private int buldco;    /* 건물_수             */
	private int buldsubco; /* 부속_건물_수        */
	private int hosilco;   /* 호실_수(호실)       */
	private int parkngco;  /* 주차_수             */
	private int hasco ; 
	
	private String totalRegstrPk ;
	private String buldRegstrPk ; 
	private String pkeyTy; /* PK_유형 */
	private String buldgb ; 
	private String cplxAt ;
	private String arcd ; 
	private String legcd ; 
	private String bunjib ; 
	private String bunjij ; 
	private String rdcode ;
	private String rdlegcd ; 
	private String underAt ; 
	private String confde ;
	private String crde ; 
	private String hasYn ; 
	private String batchde ; 
	private String batchAt ; 
	
	private String htbdno;     /* 관리대장_마스터_PK */
	private String buldnm;     /* 건물_명             */
	private String bdaddr;     /* 주소_지번           */
	private String rdaddr;     /* 주소_도로명         */
	private String ppscd;      /* 용도_코드           */
	private String ppsetcnm;   /* 용도_기타_명        */
	private String estcd;      /* 홈즈_용도_코드      */
	private String rdMainBun;  /* 도로명_본_번        */
	private String rdSubBun;   /* 도로명_부_번        */
	private String useYn;      /* 사용_여부           */
	
	private Float totalAr;     /* 전체_면적(연면적)   */
	private Float supplyAr;    /* 공급_면적           */
	private Float platAr;      /* 대지_면적           */
	private Float buldAr;      /* 건물_면적           */
	private Float buldLndRt;   /* 건폐율              */
	private Float bulkRt;      /* 용적률              */
	private Float nvCordLngX;  /* 네이버_좌표_위도_X  */
	private Float nvCordLatY;  /* 네이버_좌표_경도_Y  */
	private Float koCordLngY;  /* 카카오_좌표_위도_Y  */
	private Float koCordLatX;  /* 카카오_좌표_경도_X  */
	private Float cordLngX;    /* 좌표_위도_X         */
	private Float cordLatY;    /* 좌표_경도_Y         */

	private Float bulkCalcTotAr;  /* 용적률_산정_연면적  */
	
	private Long mngrno;
}
