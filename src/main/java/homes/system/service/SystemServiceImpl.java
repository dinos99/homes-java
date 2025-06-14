package homes.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.system.mapper.SystemMapper;
import homes.system.vo.DomainListVo;
import homes.system.vo.DomainVo;
import homes.system.vo.SystemReqVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class SystemServiceImpl implements SystemService {
	public Logger Log = LogManager.getLogger(SystemServiceImpl.class) ;
	
	private final SystemMapper mapper ;
	
	@Override
	@Transactional( readOnly = true )
	public CommResponseVo domainList(SystemReqVo reqVo) throws SQLException {
		Log.info("*** pageno: {}", reqVo.getPgno()) ;
		reqVo.setPage(); 
		Long t_cnt = mapper.selectDomainListCount(reqVo) ;
		List<CommonMap> dataList = mapper.selectDomainList(reqVo) ;
		return new CommResponseVo(t_cnt, reqVo.getPgno(), null, dataList) ;
	}

	@Transactional( readOnly = true )
	public List<CommonMap> getDefaultDomain() {
		List<CommonMap> defList = new ArrayList<CommonMap>() ; 
		/* 9개항목 Default */
		CommonMap def_domain = new CommonMap() ;
		String[] colnms = {"FST_UPLOAD_DT", "FST_UPLOAD_USER_NO", "LST_CHNG_DT", "LST_CHNG_USER_NO", "BATCH_YN"
				           , "CREATE_USER_NO", "CREAT_DT", "UPDT_USER_NO", "UPDT_DT" } ;
		String[] comments = {"최초_업로드_일시", "최초_업로드_사용자_번호", "최종_변경_일시", "최종_변경_사용자_번호", "배치_여부"
				           , "생성_사용자_번호", "생성_일시", "수정_사용자_번호", "수정_일시" } ; 
		for ( int i = 0; i < colnms.length; i ++ ) {
			String colnm   = colnms[i] ;
			String comment = comments[i] ;  
			DomainVo dvo = new DomainVo() ; 
			dvo.setColnm(colnm);
			dvo.setColcomment(comment) ;
			def_domain = mapper.selectExistsDomain(dvo) ;
			defList.add(def_domain) ;
		}
		return defList ; 
	}

	@Override
	@Transactional( readOnly = true )
	public List<CommonMap> selectExistsDomainList( DomainListVo dmListVo ) throws SQLException {
		List<CommonMap> domainList = new ArrayList<CommonMap>() ; 
		
		if ( dmListVo != null && dmListVo.getDomainList().size() > 0) {
			List<DomainVo> dList = dmListVo.getDomainList() ; 
			for ( int i = 0 ; i < dList.size(); i ++ ) {
				DomainVo dVo = dList.get(i) ;
				CommonMap domain = mapper.selectExistsDomain(dVo) ; 
				domainList.add(domain) ;
				
			}
		}
		
		/* 기본 9개컬럼 추가 */ 
		List<CommonMap> defList = this.getDefaultDomain() ; 
		for ( CommonMap defmap : defList ) {
			domainList.add(defmap) ;
		}
		return domainList ;
	}
	
	@Override
	@Transactional
	public int modifyDomain( DomainVo paramVo ) throws SQLException {
		String modYn = Optional.ofNullable(paramVo.getIsmodify()).orElse("N") ;
		if ( "Y".equals(modYn)) {
			return mapper.modifyDomain(paramVo) ;
		} else {
			return mapper.insertDomain(paramVo) ;
		}
	}
}
