package homes.data.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseSummaryVo {
    private Long mngrno;           /* 사용자_번호 */
    
    private String htbdno;         /* 관리대장_마스터_PK  */
    private String hbdno;          /* 관리대장_PK         */
    private String totalRegstrPk;  /* 총괄표제부_PK       */
    private String buldRegstrPk;   /* 표제부_PK           */
    private String buldgb;         /* 건물_구분           */
    private String cplxAt;         /* 단지_구분_코드      */
    private String arcd;           /* 지역_코드           */
    private String legcd;          /* 법정동_코드         */
    private String bunjib;         /* 번지_번             */
    private String bunjij;         /* 번지_지             */
    private String rdcode;         /* 도로명_코드         */
    private String rdlegcd;        /* 도로명_법정동_코드  */
    private String underAt;        /* 지상_지하_구분_코드 */
    private String rdMainBun;      /* 도로명_본_번        */
    private String rdSubBun;       /* 도로명_부_번        */
    private String crde;           /* 생성_일자           */
    private String useYn;          /* 사용_여부           */
    
    private String batchde ; 
    private String batchAt ; 
}
