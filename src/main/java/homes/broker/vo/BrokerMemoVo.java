package homes.broker.vo;

import lombok.Data;

@Data
public class BrokerMemoVo {
	private int officeno;    /* 중개사무소_번호 */
	private int brkno;       /* 중개사_번호 */
	private int schdulNo;    /* 일정관리번호_PK */
	private int memono;      /* 메모_번호 */
	private String stuffno;     /* 물건번호 */
	private String memocn;   /* 메모_내용 */
	private String schdulAt; /* 일정등록_여부_코드 */
	
	private String memodt ; 
}