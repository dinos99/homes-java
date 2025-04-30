package homes.comm.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.service.CommonService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommReqVo;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	public Logger Log = LogManager.getLogger(CommonController.class) ;  

	private final CommonService service ; 

	@PostMapping("/api/v1/common/arcode/sidocd")
	public ResponseEntity<String> selectSidoList(@RequestBody CommReqVo paramVo) {
		Log.info("*** param: {}", paramVo.getArcode()) ;
		List<CommonMap> sidoList = null ;
		try {
			sidoList = service.selectSidoList(paramVo) ;
		} catch ( SQLException e ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( RuntimeException e) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(sidoList)) ;
	}
	

	@PostMapping("/api/v1/common/arcode/select-arcode")
	public ResponseEntity<String> selectArcodeList(@RequestBody CommReqVo paramVo) {
		Log.info("*** param: {}", paramVo.getArcode()) ;
		CommonMap arList = null ;
		try {
			arList = service.selectArcodeList(paramVo) ;
		} catch ( SQLException e ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( RuntimeException e) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(arList)) ;
	}
}
