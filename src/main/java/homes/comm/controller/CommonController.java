package homes.comm.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.service.CommCodeService;
import homes.comm.service.CommonService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommCodeListVo;
import homes.comm.vo.CommCodeVo;
import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	public Logger Log = LogManager.getLogger(CommonController.class) ;  

	private final CommonService service ; 
	private final CommCodeService commCodeService ; 

	@GetMapping("/api/v1/common/arcode/sdList")
	public ResponseEntity<String> selectSdList() {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 지역코드-시도목록조회 ") ;  
		Log.info("*********************************************************************************") ;
		List<CommonMap> sdList = service.selectSidoList() ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(sdList)) ;
	}
	
	@GetMapping("/api/v1/common/arcode/sidoList")
	public ResponseEntity<String> sidoList(HttpServletRequest request) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 지역코드-시도목록조회 ") ;  
		Log.info("*********************************************************************************") ;
		List<CommonMap> sidoList = service.selectSidoList() ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(sidoList)) ;
	}
	
	@GetMapping("/api/v1/common/arcode/{arcode}")
	public ResponseEntity<String> selectArCode(@PathVariable String arcode) {
		Log.info("*** arcode: {}", arcode) ;
		CommonMap armap = null ; 
		try {
			armap = service.selectArCode(arcode) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(armap)) ;
	}

	@GetMapping("/api/v1/common/arcode/sgg/{sdcode}")
	public ResponseEntity<String> selectSggList(@PathVariable String sdcode) {
		Log.info("*** sdcode: {}", sdcode) ;
		List<CommonMap> sggList = null ; 
		try {
			sggList = service.selectSggList(sdcode) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(sggList)) ;
	}
	
	@GetMapping("/api/v1/common/arcode/emd/{arcode}")
	public ResponseEntity<String> selectEmdList(@PathVariable String arcode) {
		Log.info("*** arcode: {}", arcode) ;
		List<CommonMap> emdList = null ; 
		try {
			emdList = service.selectEmdList(arcode) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(emdList)) ;
	}
	
	@GetMapping("/api/v1/common/commcode/estate")
	public ResponseEntity<String> getEstateCodeList(@RequestBody CommReqVo paramVo) { 
		Log.info("*** api call estateList" ) ;
		CommCodeListVo estateList = null ; 
		try {
			estateList = commCodeService.getEstGroupList() ; 
		} catch ( RuntimeException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(estateList)) ;
	}
	@PostMapping("/api/v1/commcode/commCodeList")
	public ResponseEntity<String> getCommCodeList(@RequestBody CommCodeVo paramVo) { 
		Log.info("*** api call commCodeList" ) ;
		List<CommonMap> codeList = commCodeService.getCommCodeList(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(codeList)) ;
	}
	

	@GetMapping("/api/v1/commcode/{grpcd}")
	public ResponseEntity<String> getCommCodeList(@PathVariable String grpcd) {
		Log.info("*** grpcd: {}", grpcd) ;
		List<CommonMap> codeList = null ; 
		CommCodeVo paramVo = new CommCodeVo() ; 
		String[] grpcds = { grpcd } ; 
		paramVo.setGrpcds(grpcds) ;
		codeList = commCodeService.getCommCodeList(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(codeList)) ;
	}

	@GetMapping("/api/v1/commcode/ppsList")
	public ResponseEntity<String> getPpsCdList(HttpServletRequest request) {
		List<CommonMap> ppsList = commCodeService.getPpsCdList("") ;
		Log.info("*** get ppscd List: {}", ppsList) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ppsList)) ;
	}
	
	@GetMapping("/api/v1/commcode/ppscdList")
	public ResponseEntity<String> ppscdList(HttpServletRequest request) {
		List<CommonMap> ppsList = commCodeService.getppsCodeList("") ;
		Log.info("*** get ppscd List: {}", ppsList) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(ppsList)) ;
	}
}
