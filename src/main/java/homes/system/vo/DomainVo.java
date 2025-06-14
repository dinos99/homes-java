package homes.system.vo;

import lombok.Data;

@Data
public class DomainVo {
	
	public DomainVo() {
		insco = 0 ;
		modco = 0 ;
		delco = 0 ;
	}
	
	private String colnm ;
	private String colcomment ; 
	private String coldataTy ; 
	private String coldataSize ; 
	private String colsize;
	private String sourcecd ; 
	private String sourcenm ;
	private String creatid ;
	
	private String ismodify ; 
	
	private int insco ; 
	private int modco ; 
	private int delco ; 
}
