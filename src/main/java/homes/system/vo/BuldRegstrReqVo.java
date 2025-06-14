package homes.system.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import homes.batch.vo.BatchReqVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BuldRegstrReqVo {
	public final Logger Log = LogManager.getLogger(BuldRegstrReqVo.class) ;

	public BuldRegstrReqVo() {
		this.pgno = 1 ; 
		this.stno = 0 ; 
		this.edno = 10 ;
		this.offset = 10 ; 
	}

	public void setPage() {
		int pgno = this.getPgno() <= 0 ? 1 : this.getPgno() ; 
		
		int stno = (( pgno -  1) * 10 ) ; 
		int edno = 10 ;
		
		this.setPgno(pgno) ;
		this.setStno(stno) ;
		this.setEdno(edno) ;
		
//		Log.info("*** [ pgno: {}, from stno[{}] to edno[{}] ", pgno, stno, edno) ;
	}
	
	
	private int pgno ; 
	private int stno ;
	private int edno ; 
	private int offset ;

	private String regstrpk ; 
}
