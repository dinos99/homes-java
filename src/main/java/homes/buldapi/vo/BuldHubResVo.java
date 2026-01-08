package homes.buldapi.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuldHubResVo {
	
	private int insco = 0 ; 
	private int updco = 0 ; 
	private int delco = 0 ; 
	
	private Long mngrno ;
	
	private String message ; 
}
