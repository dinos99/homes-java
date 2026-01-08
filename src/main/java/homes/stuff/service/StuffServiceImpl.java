package homes.stuff.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.api.buld.service.BuldApiService;
import homes.api.buld.vo.BaseOutlineVo;
import homes.api.buld.vo.BuldApiReqVo;
import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.buld.mapper.BuldMapper;
import homes.comm.util.JwtUtil;
import homes.comm.util.ObjectUtil;
import homes.comm.util.StringUtil;
import homes.comm.vo.AccessTokenVo;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.owner.vo.OwnerVo;
import homes.stuff.mapper.StuffMapper;
import homes.stuff.vo.StuffListVo;
import homes.stuff.vo.StuffVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StuffServiceImpl implements StuffService {
	public final Logger Log = LogManager.getLogger(StuffServiceImpl.class) ;
	
	private final Environment env ; 
	private final StuffMapper mapper ;
	private final BuldMapper buldMapper ;
	private final BuldApiService apiService ; 
	private final JwtUtil tokenUtil = new JwtUtil() ;
	
	@Override
	@Transactional(readOnly = true)
	public CommonMap selectBrkStuffList(HttpServletRequest request, StuffVo paramVo) {
		CommonMap stuffmap = new CommonMap() ;
		List<CommonMap> cplxList = null ;
		/* Token 정보조회 */
		String token = StringUtil.getStringValue(request, "accessToken") ;
		AccessTokenVo tokenVo = tokenUtil.getTokenInfo(token) ;
		paramVo.setBrkno(tokenVo.getUserno()) ;
		
		try {
			/* 중개사 단지목록 조회 */ 
			cplxList =  mapper.selectBrkComplexList(paramVo) ; 
			stuffmap.put("cplxList", cplxList) ;
		} catch (SQLException e) {
			Log.error("StuffServiceImpl Error", e.getMessage());
			throw new HomesException(e.getErrorCode(), "[Error-" + e.getErrorCode() + "]", "데이터조회중 에러발생") ;
		}
		return stuffmap ; 
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<CommonMap> selectBrkStuff(StuffVo paramVo) {
		return mapper.selectBrkStuff(paramVo) ; 
	}
	
	@Override
	@Transactional(readOnly = true) 
	public List<CommonMap> selectBrkBlockList( StuffVo paramVo ) {
		List<CommonMap> blockList = null ; 
//		List<CommonMap> blockList = new ArrayList<CommonMap>() ; 
		try {
			blockList = mapper.selectBrkBlockList(paramVo) ; 
			/*
			for ( CommonMap block : bdList ) {
				CommonMap binfo = new CommonMap() ; 
				String buldno = block.getStringValue("buldno") ;
				Log.info("*** buldno: {}", buldno) ;
				binfo.put("buldno" , buldno) ; 
				binfo.put("blockno", block.get("blockno")) ; 
				binfo.put("blocknm", block.get("blocknm")) ;
				paramVo.setBuldno(buldno);
				paramVo.setFlgbcd("10"); 
				CommonMap ucount = mapper.selectFloorCount(paramVo) ; 
				paramVo.setFlgbcd("20"); 
				CommonMap fcount = mapper.selectFloorCount(paramVo) ; 
				paramVo.setFlgbcd("30"); 
				CommonMap rcount = mapper.selectFloorCount(paramVo) ;
				binfo.put("ucount", ucount) ; 
				binfo.put("fcount", fcount) ; 
				binfo.put("rcount", rcount) ; 
				blockList.add(binfo) ;
			}
			*/
		} catch (SQLException e) {
			Log.error("StuffServiceImpl Error", e.getMessage());
			throw new HomesException(e.getErrorCode(), "[Error-" + e.getErrorCode() + "]", "데이터조회중 에러발생") ;
		}
		return blockList ; 
	}

	public CommonMap getDefaultFloorCo(StuffVo paramVo, String flgbcd ) throws SQLException {
		paramVo.setFlgbcd(flgbcd) ; 
		CommonMap count = mapper.selectFloorCount(paramVo) ;
		if ( count == null ) {
			count = new CommonMap() ;
			count.put("buldno" , paramVo.getBuldno()) ; 
			count.put("floorCo", 0) ;
			count.put("roomCo" , 0) ; 
		}
		return count ; 
	}
	
	@Override
	@Transactional(readOnly = true) 
	public CommonMap selectFloorRoomInfo( StuffVo paramVo ) {
		CommonMap frinfo = new CommonMap() ; 
		try {
			CommonMap under = this.getDefaultFloorCo(paramVo, "10") ; /* 지하층 검색 */
			CommonMap floor = this.getDefaultFloorCo(paramVo, "20") ; /* 지상층 검색 */ 
			CommonMap rftop = this.getDefaultFloorCo(paramVo, "30") ; /* 옥탑층 검색 */ 

			for ( int i = 0; i < under.getIntValue("floorCo"); i ++ ) {
				int flno = i + 1 ;  
				paramVo.setFlgbcd("10");
				paramVo.setFlno(flno) ;
				paramVo.setFloorCo(under.getIntValue("floorCo"));
				paramVo.setRoomCo(under.getIntValue("roomCo"));
				List<CommonMap> rmList = mapper.selectFloorRoomList(paramVo) ;
				under.put("floor" + flno, rmList) ; 
			} 
			for ( int i = 0; i < floor.getIntValue("floorCo"); i ++ ) {
				int flno = i + 1 ;  
				paramVo.setFlgbcd("20");
				paramVo.setFlno(flno) ;
				paramVo.setFloorCo(floor.getIntValue("floorCo"));
				paramVo.setRoomCo(floor.getIntValue("roomCo"));
				List<CommonMap> rmList = mapper.selectFloorRoomList(paramVo) ;
				floor.put("floor" + flno, rmList) ; 
			}
			for ( int i = 0; i < rftop.getIntValue("floorCo"); i ++ ) {
				int flno = i + 1 ;  
				paramVo.setFlgbcd("30");
				paramVo.setFlno(flno) ;
				paramVo.setFloorCo(rftop.getIntValue("floorCo"));
				paramVo.setRoomCo(rftop.getIntValue("roomCo"));
				List<CommonMap> rmList = mapper.selectFloorRoomList(paramVo) ;
				rftop.put("rftop" + flno, rmList) ; 
			} 
			
			frinfo.put("under", under) ; 
			frinfo.put("floor", floor) ; 
			frinfo.put("rftop", rftop) ; 
		} catch (SQLException e) {
			Log.error("StuffServiceImpl Error", e.getMessage());
			throw new HomesException(e.getErrorCode(), "[Error-" + e.getErrorCode() + "]", "데이터조회중 에러발생") ;
		}
		return frinfo ; 
	}

	@Override
	@Transactional(readOnly = true) 
	public List<CommonMap> selectStuffOwnerList(StuffVo paramVo) {
		return mapper.selectStuffOwnerList(paramVo) ;
	}

	@Override
	@Transactional(rollbackFor = Exception.class) 
	public int updateBrkSttuf( Long brkno, StuffListVo paramVo ) {
		int up_co = 0 ; 
		for ( StuffVo vo : paramVo.getStuffListVo() ) {
			vo.setBrkno(brkno);
			vo.setSfsttus("D");
			up_co = mapper.updateBrkStuffSttus(vo) ; 
		}
		for ( OwnerVo vo : paramVo.getOwnerListVo()) {
			vo.setBrkno(brkno);
			up_co = mapper.updateBrkOwner(vo) ;
		}
		return up_co ;
	}

	@Override
	@Transactional(rollbackFor = Exception.class) 
	public List<CommonMap> insertStuff(StuffVo paramVo) {
		/* 해당물건이 존재하는지 확인 */ 
		String is_stuff = mapper.isExistsStuff(paramVo) ;
		String sfno = "" ; 
		if ("N".equals(is_stuff)) {
			sfno = mapper.createStuffno(paramVo) ;
			paramVo.setStuffno(sfno);
			mapper.insertHbdStuff(paramVo) ;
		} else {
			sfno = mapper.getStuffno(paramVo) ;
			paramVo.setStuffno(sfno);
		}
		String sfsttus = paramVo.getSfsttus() ; 
		if ( "T".equals(sfsttus)) {
			mapper.insertBrkStuff(paramVo) ;
		}
		CommonMap omap = mapper.isExistsOwner(paramVo) ; 
		Log.info("omap: {}", omap) ;
		String owno = omap.getStringValue("owno") ; 
		int owseq = omap.getIntValue("owseq") ;
		owseq ++ ; 
		paramVo.setOwseq(owseq);
		if ("000".equals(owno)) {
			owno = mapper.createOwno(paramVo) ; 
			paramVo.setOwno(owno);
			mapper.insertBrkOwner(paramVo) ;
		} 
		
		/* 용도가 변경되었는지 조회한다. */ 
		Log.error("*** 용도코드 변경조회: {}", paramVo.getPpscd()) ; 
		CommonMap upmap = buldMapper.isUpdatePpsCd(paramVo) ; 
		String isUpdated = upmap.getStringValue("isUpdated") ; 
		if ( "Y".equals(isUpdated)) {
			int histno = upmap.getIntValue("histno") ;
			paramVo.setHistno(histno);
			buldMapper.insertLedgrPpscdHist(paramVo) ; 
			buldMapper.updateLedgrPpscd(paramVo) ; 
		}
		
		return mapper.selectStuffOwnerList(paramVo) ;
	}
	@Override
	@Transactional(rollbackFor = Exception.class) 
	public List<CommonMap> deleteOwner(StuffVo paramVo) {
		mapper.deleteBrkOwner(paramVo) ;
		return mapper.selectStuffOwnerList(paramVo) ;
	}

	@Override
	@Transactional(readOnly = true)
	public CommonMap selectPostcodeBuld(StuffVo paramVo) {
		/* 집합건물인지 확인한다. */ 
		CommonMap gbdmap = mapper.selectBuldGroup(paramVo) ; 
		return gbdmap ; 
	}

	@Override
	public BaseOutlineVo getApiBaseOutlineinfo( StuffVo paramVo ) {
		/* 건축물 Hub API로부터 기본개요 정보조회 */
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ; 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBun());
		reqVo.setBunjij(paramVo.getJi());
		reqVo.setBuldnm(paramVo.getBuldnm());
		reqVo.setNumrows(1); /* 1건만 조회 */
		return apiService.getBaseOutlineinfo("/getBrBasisOulnInfo", reqVo) ;
	}

	/* 홈즈_건물마스터 생성관련 */
	@Override
	@Transactional( rollbackFor = Exception.class )
	public RecapLedgrVo getHomesBuldMaster( StuffVo paramVo ) {
		/* 관련지번 조회 */ 
		int has_co = mapper.selectRelatedJibunCount(paramVo) ;
		RecapLedgrVo db_recapVo = mapper.selectHbdRegstrMaster(paramVo) ; /* 홈즈_관리대장_총괄표제부 조회 */ ;
		return db_recapVo ; 
	}
	
	public TitleLedgrVo getApiTitleLedgrinfo( StuffVo paramVo ) {
		
		/* 건축물 Hub API로부터 기본개요 정보조회 */
		BuldApiReqVo reqVo = new BuldApiReqVo(env) ; 
		reqVo.setArcd(paramVo.getArcd());
		reqVo.setLegcd(paramVo.getLegcd());
		reqVo.setBunjib(paramVo.getBun());
		reqVo.setBunjij(paramVo.getJi());
		reqVo.setBuldnm(paramVo.getBuldnm());
		reqVo.setNumrows(1); /* 1건만 조회 */
		TitleLedgrVo vo = apiService.getTitleLedgrinfo("/getBrTitleInfo", reqVo) ;
		
		return vo ; 
	}

	@SuppressWarnings("unchecked")
	public List<TitleLedgrVo> getApiTitleLedgrList( StuffVo paramVo ) {
		String arcd   = paramVo.getArcd() ; 
		String legcd  = paramVo.getLegcd() ; 
		String bunjib = paramVo.getBun() ;
		String bunjij = paramVo.getJi() ; 
		String htbdno = paramVo.getHtbdno() ; 
		
		
		/* 건축물 Hub API로부터 기본개요 정보조회 */
		BuldApiReqVo reqVo = new BuldApiReqVo(env, arcd, legcd, bunjib, bunjij, 100) ;
		/* buldno 채번 */ 
		int bdseq = mapper.getLedgrCount(paramVo) + 1 ; 
		List<TitleLedgrVo> ledgrList = new ArrayList<TitleLedgrVo>() ; 
		int in_co = 0 ; 
		
		reqVo.setPage(1) ; 
		CommonMap ledmap = apiService.getTitleLedgrList("/getBrTitleInfo", reqVo) ;
		CommonMap pginfo = (CommonMap)ledmap.get("pginfo") ; 
		int t_count = pginfo.getIntValue("tcnt") ;     /* 전체개수 */
		int numrows = pginfo.getIntValue("numrows") ;  /* 페이지당 100건씩 조회 */ 
		int page    = pginfo.getIntValue("page") ;     /* 현재 페이지 */
		double d_pg = Math.ceil((double) t_count / numrows);		
		int t_page  = (int) d_pg ; /* 전체 페이지 */

		List<TitleLedgrVo> tList = ( List<TitleLedgrVo> )ledmap.get("ledgrList") ;
//		Log.info("*** tList size:{}, {}/{}, total: {}, ledgrList size: {}", tList.size(), page, t_page, t_count, ledgrList.size()) ; 
		while ( page <= t_page ) {
			for ( TitleLedgrVo tvo : tList ) {
				String buldno = "" ; 
				// * buldno = prefix(3) + buldgb(2) + arcd(5) + legcd(5) + bun(4) + ji(4) + seq(10) * /
				buldno = buldno + "HBD" + StringUtil.strLpad(paramVo.getBuldgb(), 2, '0') ; 
				buldno = buldno + arcd + legcd ; 
				buldno = buldno + bunjib + bunjij ; 
				buldno = buldno + StringUtil.strLpad(String.valueOf(bdseq), 10,'0') ; 
				
				tvo.setBuldno(buldno) ;
				tvo.setHtbdno(htbdno) ; 
				tvo.setUseYn("Y") ;
				tvo.setBatchYn("N") ;
				tvo.setBrkno(paramVo.getBrkno()) ; 
				in_co = in_co + mapper.insertHbdLedgr(tvo) ; 
				tvo.setInco(in_co) ;
				ledgrList.add(tvo) ;
				bdseq ++ ;
			}
			page ++ ;
			if ( page <= t_page ) {
				reqVo.setPage(page) ;
				ledmap = apiService.getTitleLedgrList("/getBrTitleInfo", reqVo) ;
				tList = ( List<TitleLedgrVo> )ledmap.get("ledgrList") ;
//				Log.info("*** tList size:{}, {}/{}, total: {}, ledgrList size: {}", tList.size(), page, t_page, t_count, ledgrList.size()) ;
			}
		}
//		Log.info("*** tList size:{}, {}/{}, total: {}, ledgrList size: {}", tList.size(), page, t_page, t_count, ledgrList.size()) ;
		return ledgrList ; 
	}
	
	@Override
	@Transactional(readOnly = true)
	public TitleLedgrVo selectTitleLedgrinfo(StuffVo paramVo) {
		
		/* 통합DB(표제부) 등록정보 확인 */
		int in_cnt = mapper.getHbdTitleLedgrCount(paramVo) ;
		if ( in_cnt == 0 ) {
			TitleLedgrVo bdvo = getApiTitleLedgrinfo( paramVo ) ;
			return bdvo ; 
		} else {
			/* DB정보 조회 후 리턴 ( 나중에 하자 ) */
			TitleLedgrVo bdvo = new TitleLedgrVo() ;			
			return bdvo ; 
		}
	}

	@Override
	public TitleLedgrVo manageHomesTitleLedgr( StuffVo paramVo ) {
		TitleLedgrVo bdVo = new TitleLedgrVo() ; 
		/* 관련지번 조회 */ 
		CommonMap remap = mapper.selectMasterbunji(paramVo) ;
		if ( ObjectUtil.isNotEmpty(remap)) {
			/* 관련지번이 존재하면 대표지번으로검색 */
			paramVo.setBun(remap.getStringValue("bunjib"));
			paramVo.setJi(remap.getStringValue("bunjij"));
		} 
		return bdVo ; 
	}

	@Override
	@Deprecated
	public CommonMap manageHomesBuldLedgr( StuffVo paramVo ) {
		List<CommonMap> raList = new ArrayList<CommonMap>() ;
		/* 대표지번 조회 */ 
		CommonMap remap = mapper.selectMasterbunji(paramVo) ;
		raList.add(remap) ; /* 대표지번 우선 입력 */
		/* 대표지번으로검색 */
		paramVo.setBun(remap.getStringValue("bunjib"));
		paramVo.setJi(remap.getStringValue("bunjij"));
		/* 관련지번목록 조회 */ 
		paramVo.setRdcode(remap.getStringValue("rdcode"));
		List<CommonMap> relatedList = mapper.selectRelatedbunji(paramVo) ; 
		if ( relatedList != null && relatedList.size() > 0) {
			for ( CommonMap cmap : relatedList ) raList.add(cmap) ;
		}
		paramVo.setRemap(raList);
		
		/* ***********************************************************************
		 * 드디어 시작함 .... 표제부 검색
		 * ***********************************************************************/
		paramVo.setUseYn("");
		/* 표제부가 없으명 API 조회한다(관련지번포함) */ 
		for ( CommonMap rmap : raList ) {

			paramVo.setRdcode(rmap.getStringValue("rdcode"));
			paramVo.setBun(rmap.getStringValue("bunjib"));
			paramVo.setJi(rmap.getStringValue("bunjij"));
			paramVo.setUseYn("Y");
			int cnt = mapper.getLedgrCount(paramVo) ; 
			if ( cnt == 0 ) {
				Log.info("*** Ledgr count: {}", cnt) ; 
//				List<TitleLedgrVo> tList = getApiTitleLedgrList( paramVo ) ; /* 페이지당 100건씩 조회 */
			}
			break ; 
		}
		
		return remap ; 
	}


	@Override
	@Transactional( readOnly = true )
	public List<TitleLedgrVo> selectHbdLedgr(StuffVo paramVo) {
		int has_cnt = mapper.selectRelatedJibunCount(paramVo) ; 
		paramVo.setHasRelcount(has_cnt);
		return mapper.selectHbdLedgr(paramVo);
	}
}
