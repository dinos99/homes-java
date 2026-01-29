package homes.stuff.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.api.buld.vo.RecapLedgrVo;
import homes.api.buld.vo.TitleLedgrVo;
import homes.broker.vo.BrokerVo;
import homes.comm.util.JsonUtil;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.stuff.service.StuffService;
import homes.stuff.vo.StuffListVo;
import homes.stuff.vo.StuffVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StuffController {
	public final Logger Log = LogManager.getLogger(StuffController.class) ;

	private final StuffService service ;
	
	@PostMapping("/api/v1/stuff/stuffList")
	public ResponseEntity<String> stuffList(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		Log.error("*** api call: /api/v1/stuff/stuffList") ;
		CommonMap stuffList = service.selectBrkStuffList(request, paramVo);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(stuffList)) ;
	}
	@PostMapping("/api/v1/stuff/brker-stuff")
	public ResponseEntity<String> brkerStuff(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		List<CommonMap> stuffList = service.selectBrkStuff(request, paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(stuffList)) ;
	}
	

	@PostMapping("/api/v1/stuff/blockList")
	public ResponseEntity<String> blockList(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		List<CommonMap> blockList = service.selectBrkBlockList(request, paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(blockList)) ;
	}

	@PostMapping("/api/v1/stuff/buld-struct")
	public ResponseEntity<String> buldStruct(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		BrokerVo bvo = RequestUtil.getBroker(request) ; 
		paramVo.setBrkno(bvo.getBrokerno()) ;
		paramVo.setOfficeno(bvo.getOfficeno()) ; 
		CommonMap flinfo = service.selectFloorRoomInfo(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(flinfo)) ;
	}	
	@PostMapping("/api/v1/stuff/buld-Ledgr-info")
	public ResponseEntity<String> buldLedgrinfo(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		BrokerVo bvo = RequestUtil.getBroker(request) ; 
		paramVo.setBrkno(bvo.getBrokerno()) ;
		paramVo.setOfficeno(bvo.getOfficeno()) ; 
		CommonMap bdinfo = service.selectLedgrinfo(paramVo) ;  
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(bdinfo)) ;
	}

	
	@PostMapping("/api/v1/stuff/ownerList") 
	public ResponseEntity<String> ownerList(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		int brkno = RequestUtil.getUserno(request) ; 
		paramVo.setBrkno(brkno) ; 
		List<CommonMap> ownerList = service.selectStuffOwnerList(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ownerList)) ;
	}

	@PostMapping("/api/v1/stuff/add-stuff") 
	public ResponseEntity<String> addStuff(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		int brkno = RequestUtil.getUserno(request) ; 
		paramVo.setBrkno(brkno) ;
		List<CommonMap> ownerList = null ; 
		try {
			ownerList = service.insertStuff(paramVo) ;
		} catch ( HomesException he ) {
			Log.error("*** 중개사 물건등록 에러 ") ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(he.getCode(), he.getMessage()));
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ownerList)) ;
	}

	@PostMapping("/api/v1/stuff/delete-owner") 
	public ResponseEntity<String> deleteOwner(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		int brkno = RequestUtil.getUserno(request) ; 
		paramVo.setBrkno(brkno) ;
		List<CommonMap> ownerList = null ; 
		try {
			ownerList = service.deleteOwner(paramVo) ;
		} catch ( HomesException he ) {
			Log.error("*** 중개사 소유주삭제 에러 ") ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(he.getCode(), he.getMessage()));
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ownerList)) ;
	}
	

	@PostMapping("/api/v1/stuff/update-stuff")
	public ResponseEntity<String> updateBrkSttuf(HttpServletRequest request, @RequestBody StuffListVo paramVo ) {
		Log.error(paramVo) ;
//		int brkno = RequestUtil.getUserno(request) ; 
		CommonMap result = new CommonMap() ; 
		try {
			int up_co = 0 ; 
//			int up_co = service.updateBrkSttuf(brkno, paramVo) ;
			result.put("up_co", up_co) ; 
		} catch ( Exception e ) {
			e.printStackTrace() ; 
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(result)) ;
	}
	
	/**
	 * 통합DB(홈즈_관리대장_마스터) 검색
	 * @param paramVo
	 * @return
	 */
	@PostMapping("/api/v1/stuff/total-buld")
	public ResponseEntity<String> selectTitleLedgrinfo(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		Log.info("*** 건축물대장 통합DB조회 **********************************************");
		int brkno = RequestUtil.getUserno(request) ;
		paramVo.setBrkno(brkno);
		RecapLedgrVo recapVo = service.getHomesBuldMaster( paramVo ) ;
		paramVo.setHtbdno(recapVo.getHtbdno()) ; 
		paramVo.setBuldgb(recapVo.getBuldgb()) ;
		List<TitleLedgrVo> titleLedgrList = service.selectHbdLedgr(paramVo) ; 
//		CommonMap buldinfo = service.manageHomesBuldLedgr( paramVo ) ;
		CommonMap buldinfo = new CommonMap() ;
		buldinfo.put("recap"    , recapVo) ;        /* 홈즈_관리대장_총괄표제부 */
		buldinfo.put("titleList", titleLedgrList) ; /* 홈즈_관리대장_표제부 */
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(buldinfo)) ;
	}

	@PostMapping("/api/v1/stuff/insert-stuff") 
	public ResponseEntity<String> insertStuff(HttpServletRequest request, @RequestBody StuffVo[] paramVo ) {
		CommonMap ins_map = service.insertStuff(request, paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ins_map)) ;
	}
	
	@PostMapping("/api/v1/stuff/stuff-info")
	public ResponseEntity<String> stuffInfo(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		CommonMap bdstuff = service.selectBrkStuffBuld(request, paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(bdstuff)) ;
	}

	@PostMapping("/api/v1/stuff/stuff-buld-info")
	public ResponseEntity<String> stuffBuldInfo(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		List<CommonMap> bdstuff = service.selectStuffBuldinfo(request, paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(bdstuff)) ;
	}
}
