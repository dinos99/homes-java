package homes.comm.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.mapper.CommCodeMapper;
import homes.comm.vo.CommCodeListVo;
import homes.comm.vo.CommCodeVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames = "CommCode")
public class CommCodeServiceImpl implements CommCodeService {
	
	public Logger Log = LogManager.getLogger(CommonServiceImpl.class) ;

	private final CommCodeMapper mapper ; 
	
    @Cacheable(key = "'all'")
	@Transactional(readOnly = true)
    public CommCodeListVo getCodeAllList(String grpcd) {
    	CommCodeVo pCodeVo = new CommCodeVo() ; 
    	pCodeVo.setGrpcd(grpcd);
        List<CommCodeVo> codeList = mapper.getCodeAllList(pCodeVo) ;
        Log.info("Repository getCodeAllList {}", codeList);
        return new CommCodeListVo(codeList);
    }
    
    @Cacheable(key = "'codegroup'")
	@Transactional(readOnly = true)
    public CommCodeListVo getCodeGroupList(String grpcd) {
    	CommCodeVo pCodeVo = new CommCodeVo() ; 
    	pCodeVo.setGrpcd(grpcd);
        List<CommCodeVo> codeList = mapper.getCodeGroupList(pCodeVo) ;
        Log.info("Repository getCodeGroupList {}", codeList);
        return new CommCodeListVo(codeList);
    } 

    /**
     * 부동산구분 코드목록조회 ( GRP_CD = 'EST' ) 
     * @return CommCodeListVo
     */
    @Cacheable(key = "'estgroup'")
	@Transactional(readOnly = true)
    public CommCodeListVo getEstGroupList() {
    	CommCodeVo pCodeVo = new CommCodeVo() ; 
    	pCodeVo.setGrpcd("EST");
        List<CommCodeVo> codeList = mapper.getCodeGroupList(pCodeVo) ;
        Log.info("Repository getEstGroupList {}", codeList);
        return new CommCodeListVo(codeList);
    }  
}
