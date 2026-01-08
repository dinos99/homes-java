package homes.manager.vo;

import lombok.Data;

@Data
public class TodoVo {
	private String wkid ; 
	private String stde ; 
	private String edde ; 
	private String doneYn ; 
	private String wkcont ;
	
	private Long mngrno ;
	private Long userno ; 
}
