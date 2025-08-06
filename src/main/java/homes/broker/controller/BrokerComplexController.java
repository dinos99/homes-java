package homes.broker.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.broker.service.BrokerComplexService;
import homes.broker.vo.BrokerComplexVo;
import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BrokerComplexController {
	public Logger Log = LogManager.getLogger(BrokerComplexController.class) ;  
	
	private final BrokerComplexService service ; 
	
	@PostMapping("/api/v1/broker/complexList")
	public ResponseEntity<String> complexList(@RequestBody BrokerComplexVo paramVo ) {
		List<CommonMap> complexList = null ; 
		try {
			complexList = service.selectComplexList(paramVo) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(complexList)) ;
	}
	
}
