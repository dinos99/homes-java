package homes.popup.vo;

import lombok.Data;

@Data
public class PopCommReqVo {

	public PopCommReqVo() {
		this.pageno = 1 ; 
		this.stno = 0 ; 
		this.edno = 10 ;
		this.offset = 10 ; 
	}
	
	int pageno ; 
	int stno ;
	int edno ; 
	int offset ;  
	
	/* 담당자 검색 */
	String usernm ; 
}
