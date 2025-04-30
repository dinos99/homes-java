package homes.popup.vo;

import java.util.List;

import homes.comm.vo.CommonMap;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PopCommResVo {

	private CommonMap page ; 
	private List<CommonMap> dataList ;
}
