package homes.batch.vo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchVo {

	public BatchVo(String id) {
		this.jobid   = id ;
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
	private String batchYn ;
	private String message ; 
	
	private Long mngrno ; 
	
	private double exco ;
	private double spco ; 
	
}
