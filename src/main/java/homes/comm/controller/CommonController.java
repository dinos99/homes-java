package homes.comm.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.service.CommCodeService;
import homes.comm.service.CommonService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommCodeListVo;
import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	public Logger Log = LogManager.getLogger(CommonController.class) ;  

	private final CommonService service ; 
	private final CommCodeService commCodeservice ; 
	
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

	@GetMapping("/api/v1/common/arcode/sdList")
	public ResponseEntity<String> selectSdList() {
		List<CommonMap> sdList = null ; 
		try {
			sdList = service.selectSidoList() ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(sdList)) ;
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
			estateList = commCodeservice.getEstGroupList() ; 
		} catch ( RuntimeException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(estateList)) ;
	}

	@GetMapping("/api/v1/commcode/{grpcd}")
	public ResponseEntity<String> getCommCodeList(@PathVariable String grpcd) {
		Log.info("*** grpcd: {}", grpcd) ;
		List<CommonMap> codeList = null ; 
		try {
			codeList = service.getCommCodeList(grpcd) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(codeList)) ;
	}
	
}
