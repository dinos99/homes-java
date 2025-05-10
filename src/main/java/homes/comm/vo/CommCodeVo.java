package homes.comm.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommCodeVo {

	private String code ;
	private String grpcd ; 
	private String uppercd ; 
	private String codenm ;
	
	public CommCodeVo( String gcd, String nm , String upcd ) {
		this.grpcd = gcd ; 
		this.codenm = nm ;
		this.uppercd = upcd ;
	}
}
