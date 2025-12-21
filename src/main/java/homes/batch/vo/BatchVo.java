package homes.batch.vo;
import homes.comm.constants.EnumBatchJob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchVo {

	public BatchVo(String id) {
		this.jobid   = id ;
		this.batchty = id ; 
		this.sttuscd = EnumBatchJob.BTS_READY.getCode() ;
		this.filenm  = "" ; 
		this.message = "" ; 
		this.exco    = 0 ; 
		this.spco    = 0 ; 
		this.mngrno  = 0l ;
		this.batchYn = "Y" ;
	}

	public BatchVo(String id, String batchYn) {
		this.jobid   = id ;
		this.exco    = 0 ; 
		this.spco    = 0 ;  
		this.mngrno  = 0l ;
		this.batchYn = batchYn ;
	}

	public BatchVo(String id, String batchYn, Long mno ) {
		this.jobid   = id ;
		this.exco    = 0 ; 
		this.spco    = 0 ;  
		this.mngrno  = mno ;
		this.batchYn = batchYn ;
	}
	
	private String jobid ;
	private String batchty ;
	private String sttuscd ; 
	private String uuid ; 
	private String filenm ; 
	private String batchYn ;
	private String message ; 
	private String batchde ; 
	private String batchAt ; 
	
	private Long mngrno ; 
	
	private double exco ;
	private double spco ; 
	
}
