package homes.broker.vo;

import lombok.Data;

@Data
public class BrokerBuldVo {
	private int officeno ; /* 중개사무소번호 */
	private int brkno ;    /* 중개사번호     */
	private int mngrno ;   /* 생성/수정 사용자번호 */
	private int reqno ;    /* 의뢰번호       */
	private int schdno ;   /* 일정번호       */
	
	private String hpsno ;    /* 홈즈_건물대장_전유부_PK */
	private String occpgb;    /* 점유_구분_코드          */
	private String expirede ; /* 만기_일자               */
	private String crtcyy ;   /* 계약기간_년             */
	private String crtcmm ;   /* 계약기간_월             */
	private String crtcAt ;   /* 계약기간_구분_코드      */
	
	private String col ; /* 변경대상 컬럼 */ 
}
