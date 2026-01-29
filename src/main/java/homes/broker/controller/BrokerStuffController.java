package homes.broker.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.broker.service.BrokerStuffService;
import homes.broker.vo.BrokerBuldVo;
import homes.broker.vo.BrokerStuffVo;
import homes.broker.vo.BrokerVo;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.JsonUtil;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommonMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BrokerStuffController {
	public Logger Log = LogManager.getLogger(BrokerStuffController.class) ;  
	
	private final BrokerStuffService service ; 

	@PostMapping("/api/v1/broker/getPublicList")
	public ResponseEntity<String> getPublicList(HttpServletRequest request, @RequestBody BrokerStuffVo paramVo ) {
		BrokerVo brkVo = RequestUtil.getBroker(request) ; 
		paramVo.setBrkno(brkVo.getBrokerno()) ;
		paramVo.setOfficeno(brkVo.getOfficeno()) ; 
		
		Log.info("*********************************************************************************") ; 
		Log.info("*** Broker 공동주택(단지내) 물건조회") ;
		Log.info("*** Broker User   : {}", brkVo.getBrokerno()) ; 
		Log.info("*** [current time : {}]", DateTimeUtil.getCurrentDateTime()) ;  
		Log.info("*********************************************************************************") ;
		List<CommonMap> pbList = service.getPublicHouseList(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(pbList)) ;
	}

	@PostMapping("/api/v1/broker/stuff/regist")
	public ResponseEntity<String> insBrkComplex(HttpServletRequest request, @RequestBody BrokerStuffVo paramVo ) {
		BrokerVo brkVo = RequestUtil.getBroker(request) ; 
		paramVo.setBrkno(brkVo.getBrokerno()) ;
		paramVo.setOfficeno(brkVo.getOfficeno()) ; 

		CommonMap cmap = new CommonMap() ; 
		int ins_co = service.insertBrokerStuff(paramVo) ; 
		cmap.put("insco", ins_co) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(cmap)) ;
	}
	
	@PostMapping("/api/v1/broker/buld/update-buld-info")
	public ResponseEntity<String> updateBuldInfo(HttpServletRequest request, @RequestBody BrokerBuldVo paramVo ) {
		BrokerVo brkVo = RequestUtil.getBroker(request) ; 
		paramVo.setBrkno(brkVo.getBrokerno()) ;
		paramVo.setOfficeno(brkVo.getOfficeno()) ; 
		paramVo.setMngrno(brkVo.getBrokerno()) ; 

		CommonMap cmap = service.updateBuldInfo(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(cmap)) ;
		
	}
	
}
