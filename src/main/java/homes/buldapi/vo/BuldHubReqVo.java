package homes.buldapi.vo;

import lombok.Data;

@Data
public class BuldHubReqVo {

	public BuldHubReqVo() {
		this.pgno = 1 ; 
		this.stno = 0 ; 
		this.edno = 10 ;
		this.offset  = 10 ; 
		this.numrows = 10 ;
	}

	public void setPage() {
		int pgno = this.getPgno() <= 0 ? 1 : this.getPgno() ; 
		
		int stno = (( pgno -  1) * this.numrows ) ; 
//		int edno = stno + 10 ;
		int edno = this.numrows ;
		
		this.setPgno(pgno) ;
		this.setStno(stno) ;
		this.setEdno(edno) ;
		
//		Log.info("*** [ pgno: {}, from stno[{}] to edno[{}] ", pgno, stno, edno) ;
	}
		
	
	private int pgno ; 
	private int stno ;
	private int edno ; 
	private int offset ;
	private int numrows ; 
	
	private int insco = 0 ; 
	private int updco = 0 ; 
	private int delco = 0 ; 
	
	private Long mngrno ;
	
	private String htbdno ; 
	private String hbdno ; 
	
	private String totalRegstrPk ; 
	private String buldRegstrPk ; 
	
	private String arcode ; /* 지역코드 + 법정동코드 */ 
	private String arcd ;   /* 지역코드 */
	private String legcd ;  /* 법정동코드 */
	private String bunjib ; /* 번지_번 */
	private String bunjij ; /* 번지_지 */
	private String buldnm ; /* 건물명 */ 
	private String buldgb ; 
	
	private String batchAt ; 
	private String batchde ;
	
	private String selgb = "API"; /* API: API 조회, HBD: HOMES DB조회 */
}
