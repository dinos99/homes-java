package homes.broker.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.broker.service.BrokerComplexService;
import homes.broker.vo.BrokerComplexVo;
import homes.broker.vo.BrokerVo;
import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommonMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BrokerComplexController {
	public Logger Log = LogManager.getLogger(BrokerComplexController.class) ;  
	
	private final BrokerComplexService service ; 

	@PostMapping("/api/v1/broker/complex-info")
	public ResponseEntity<String> complexInfo(@RequestBody BrokerComplexVo paramVo, HttpServletRequest request ) {
		BrokerVo bvo = RequestUtil.getBroker(request) ;
		paramVo.setBrkno(bvo.getBrokerno()) ;
		paramVo.setOfficeno(bvo.getOfficeno()) ; 
		CommonMap complex = service.selectComplexinfo(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(complex)) ;
	}
	
	@PostMapping("/api/v1/broker/complexList")
	public ResponseEntity<String> complexList(@RequestBody BrokerComplexVo paramVo, HttpServletRequest request ) {
		CommonMap complex = null ; 
		try {
			int userno = RequestUtil.getUserno(request) ; 
			paramVo.setBrkno(userno) ; 
			complex = service.selectComplexList(paramVo) ;
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(complex)) ;
	}

	@PostMapping("/api/v1/broker/insBrkComplex")
	public ResponseEntity<String> insBrkComplex(@RequestBody BrokerComplexVo paramVo ) {
		int ins_co = 0 ; 
		CommonMap cmap = new CommonMap() ; 
		try {
			ins_co = service.insertBrokerComplex(paramVo) ;
			cmap.put("insco", ins_co) ; 
		} catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(cmap)) ;
	}
}
