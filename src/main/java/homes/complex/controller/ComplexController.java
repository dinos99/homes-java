package homes.complex.controller;

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
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommonMap;
import homes.complex.service.ComplexService;
import homes.complex.vo.ComplexVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ComplexController {
	public Logger Log = LogManager.getLogger(ComplexController.class) ;  
	
	private final ComplexService service ; 
	
	@PostMapping("/api/v1/complex/complexList")
	public ResponseEntity<String> getComplexList(@RequestBody ComplexVo paramVo ) {
		Log.info("*** arcode: {}, legcd: {}", paramVo.getArcode(), paramVo.getLegcd()) ; 
		List<CommonMap> cpxList = null ;
		try {
			cpxList = service.selectComplexList(paramVo) ;
		} catch ( SQLException e ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( RuntimeException e) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(cpxList)) ;
	}
}
