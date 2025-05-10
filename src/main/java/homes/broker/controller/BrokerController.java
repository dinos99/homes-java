package homes.broker.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import homes.broker.service.BrokerService;
import homes.broker.vo.BrokerOfficeVo;
import homes.broker.vo.BrokerVo;
import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommonMap;
import homes.comm.vo.FileVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BrokerController {
	public Logger Log = LogManager.getLogger(BrokerController.class) ;  
	
	private final BrokerService service ; 
	
	@PostMapping("/api/v1/broker/uploadbizfile")
	public ResponseEntity<String> Uploadbizfile(@RequestParam("userno") Long brokerno
												, @RequestParam("bizfile") MultipartFile bizfile
												, @RequestParam("estfile") MultipartFile estfile ) {
		CommonMap fmap = new CommonMap() ; 
		fmap.put("bizfile", new FileVo()) ; 
		fmap.put("estfile", new FileVo()) ; 
		try {
			FileVo bizVo = service.brokerUploadfile("/broker", brokerno, "biz-", bizfile) ; 
			FileVo estVo = service.brokerUploadfile("/broker", brokerno, "est-", estfile) ;
			
			fmap.put("bizfile", bizVo) ; 
			fmap.put("estfile", estVo) ; 
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(fmap)) ;
	}
	
	
	@PostMapping("/api/v1/broker/registbroker")
	public ResponseEntity<String> Registbroker(@RequestBody BrokerVo paramVo ) { 
		try {
			service.registbroker(paramVo) ; 
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(paramVo)) ;
	}
	
	@PostMapping("/api/v1/broker/registoffice")
	public ResponseEntity<String> RegistOffice(@RequestBody BrokerOfficeVo paramVo ) { 
		try {
			service.registbrokerOffice(paramVo) ; 
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
			return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} 
		return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(paramVo)) ;
	}

}
