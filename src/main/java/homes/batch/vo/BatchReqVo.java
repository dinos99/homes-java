package homes.batch.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Data;

@Data
public class BatchReqVo {
	public final Logger Log = LogManager.getLogger(BatchReqVo.class) ;
	
	public BatchReqVo() {
		this.pgno = 1 ; 
		this.stno = 0 ; 
		this.edno = 10 ;
		this.offset  = 10 ; 
		this.numrows = 10 ;
	}

	
	private int pgno ; 
	private int stno ;
	private int edno ; 
	private int offset ;
	private int numrows ; 
	
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
	
	private String   btstde ;
	private String   batchTy ;
}
