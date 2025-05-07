package homes.comm.vo;

import lombok.Data;

@Data
public class FileVo {
	
	private Long userno ; 
	private Long fileno ; 
	private Long  fsize ; 
	
	private String fpath ; 
	private String fsvname ;
	private String forgname ;
	private String ftype ; 
	
}
