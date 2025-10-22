package homes.stuff.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.util.JsonUtil;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.stuff.service.StuffService;
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
		CommonMap stuffList = service.selectBrkStuffList(request, paramVo);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(stuffList)) ;
	}

	@PostMapping("/api/v1/stuff/blockList")
	public ResponseEntity<String> blockList(@RequestBody StuffVo paramVo ) {
		List<CommonMap> blockList = service.selectBrkBlockList(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(blockList)) ;
	}

	@PostMapping("/api/v1/stuff/floorList")
	public ResponseEntity<String> floorList(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		Long brkno = RequestUtil.getUserno(request) ; 
		paramVo.setBrkno(brkno) ;
		CommonMap flinfo = service.selectFloorRoomInfo(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(flinfo)) ;
	}

	@PostMapping("/api/v1/stuff/ownerList") 
	public ResponseEntity<String> ownerList(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		Long brkno = RequestUtil.getUserno(request) ; 
		paramVo.setBrkno(brkno) ; 
		List<CommonMap> ownerList = service.selectStuffOwnerList(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ownerList)) ;
	}

	@PostMapping("/api/v1/stuff/add-stuff") 
	public ResponseEntity<String> addStuff(HttpServletRequest request, @RequestBody StuffVo paramVo ) {
		Long brkno = RequestUtil.getUserno(request) ; 
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
		Long brkno = RequestUtil.getUserno(request) ; 
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
	
	
}
