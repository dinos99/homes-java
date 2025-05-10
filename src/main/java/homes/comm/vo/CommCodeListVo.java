package homes.comm.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class CommCodeListVo {
	
	private List<CommCodeVo> codeList = new ArrayList<>() ; 
	
	public CommCodeListVo( List<CommCodeVo> codeList ) {
		this.codeList = codeList ; 
	}
	
}
