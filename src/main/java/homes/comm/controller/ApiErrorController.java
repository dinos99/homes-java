package homes.comm.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ApiErrorController implements ErrorController {
	
	public Logger Log = LogManager.getLogger(ApiErrorController.class) ;  
	
	@PostMapping("/api/error")
    public ResponseEntity<String> getMemberProfile(HttpServletRequest request, HttpServletResponse response) {
		HomesException he = null ; 
		
		try {
			he = ( HomesException ) request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR") ;
		} catch ( RuntimeException e ) { 
			Log.error("*** ApiError: {}", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.BAD_REQUEST.getSttusCd())) ;
		} 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(he.getCode(), null)) ; 
	}
	
}
