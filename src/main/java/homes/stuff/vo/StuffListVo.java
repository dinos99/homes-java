package homes.stuff.vo;

import java.util.List;

import homes.owner.vo.OwnerVo;
import lombok.Data;

@Data
public class StuffListVo {
	List<StuffVo> stuffListVo ; 
	List<OwnerVo> ownerListVo ; 
}
