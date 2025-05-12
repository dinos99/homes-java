package homes.project.vo;

import lombok.Data;

@Data
public class ProjectReqVo {
	
	public ProjectReqVo() {
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
		
		int stno = ( pgno / 10 ) ; 
		int edno = stno + 10 ;
		
		this.setStno(stno) ;
		this.setEdno(edno) ;
	}
	
	/* 사용자 검색 */
	private String usernm ; 
	private String userTy ;
}
