package homes.api.buld.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseOutlineVo {
 
	private String tregstrPk ; /* 총괄표제부 PK */  
	private String bregstrPk ; /* 표제부 PK */
	private String buldid ;    /* 건물 ID, 어디에 사용되는지 모르겠음 */ 
	private String buldnm ;    /* 건물 명 */ 
	private String buldgb ;    /* 건축물 구분: [1]일반건물, [2]집합건물 */
	private String bdaddr ;    /* 건물주소(지번) */
	private String rdaddr ;    /* 건물주소(도로명) */
	
}
