package homes.system.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Data;

@Data
public class SystemReqVo {
	public final Logger Log = LogManager.getLogger(SystemReqVo.class) ;
	
	public SystemReqVo() {
		this.pgno = 1 ; 
		this.stno = 0 ; 
		this.edno = 10 ;
		this.offset = 10 ; 
	}

	
	private int pgno ; 
	private int stno ;
	private int edno ; 
	private int offset ;
	
	public void setPage() {
		int pgno = this.getPgno() <= 0 ? 1 : this.getPgno() ; 
		
		int stno = (( pgno -  1) * 10 ) ; 
//		int edno = stno + 10 ;
		int edno = 10 ;
		
		this.setPgno(pgno) ;
		this.setStno(stno) ;
		this.setEdno(edno) ;
		
		Log.info("*** [ pgno: {}, from stno[{}] to edno[{}] ", pgno, stno, edno) ;
	}
	
	/* 사용자 검색 */
	private String   colnm ;  
	private String   colcomment ;
}
