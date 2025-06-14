package homes.system.vo;

import java.util.List;

import homes.comm.vo.CommonMap;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SystemResVo {
	private CommonMap page ; 
	private List<CommonMap> dataList ;
}
