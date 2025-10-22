package homes.stuff.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.util.JwtUtil;
import homes.comm.util.StringUtil;
import homes.comm.vo.AccessTokenVo;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.stuff.mapper.StuffMapper;
import homes.stuff.vo.StuffVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StuffServiceImpl implements StuffService {
	public final Logger Log = LogManager.getLogger(StuffServiceImpl.class) ;

	private final StuffMapper mapper ;
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
		
		return mapper.selectStuffOwnerList(paramVo) ;
	}
	@Override
	@Transactional(rollbackFor = Exception.class) 
	public List<CommonMap> deleteOwner(StuffVo paramVo) {
		mapper.deleteBrkOwner(paramVo) ;
		return mapper.selectStuffOwnerList(paramVo) ;
	}
}
