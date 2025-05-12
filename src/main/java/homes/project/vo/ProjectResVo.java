package homes.project.vo;

import java.util.List;

import homes.comm.vo.CommonMap;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectResVo {
	private CommonMap page ; 
	private List<CommonMap> dataList ;
}
