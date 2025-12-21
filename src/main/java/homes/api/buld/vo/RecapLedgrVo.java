package homes.api.buld.vo;

import lombok.Data;

@Data
public class RecapLedgrVo {
  // 관리대장_마스터_PK
  private String htbdno;
  // 건물구분
  private String buldgb;
  // 지역코드
  private String arcd;
  // 법정동코드
  private String legcd;
  // 번지_번
  private String bunjib;
  // 번지_지
  private String bunjij;
  // 건물_명
  private String buldnm;
  // 총괄표제부_PK
  private String buldRegstrPk;
  // 주소_지번
  private String bdaddr;
  // 주소_도로명
  private String rdaddr;
  // 용도_코드
  private String ppscd;
  // 용도_기타_명
  private String ppsetcnm;
  // 홈즈_용도_코드
  private String estcd;
  // 세대_수(세대)
  private int hshldco;
  // 가구_수(가구)
  private int fmlyco;
  // 건물_수
  private int buldco;
  // 부속_건물_수
  private int buldsubco;
  // 호실_수(호실)
  private int roomco;
  // 주차_수
  private int parkngco;
  // 대지_구분
  private String platgb;
  // 도로명_코드
  private String rdcode;
  // 도로명_법정동_코드
  private String rdlegcd;
  // 지상_지하_구분_코드
  private String underAt;
  // 도로명_본_번
  private String rdMainBun;
  // 도로명_부_번
  private String rdSubBun;
  // 전체_면적(연면적)
  private Float totalAr;
  // 공급_면적
  private Float supplyAr;
  // 대지_면적
  private Float platAr;
  // 건물_면적
  private Float buldAr;
  // 용적률_산정_연면적
  private Float bulkCalcAr;
  // 건폐율
  private Float buldLndRt;
  // 용적률
  private Float bulkRt;
  // 생성_일자
  private String crde;
  // 사용_여부
  private String useYn;
  // 배치_여부
  private String batchYn;
  
  private String arname ; 
  
  private int inco ; 
  private int flground ; /* 지상_층_수 */ 
  private int flunder ;  /* 지하_층_수 */ 
  
  private Long userno ; 
  private Long brkno ; 
}
