package homes.comm.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommonMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ApiErrorController implements ErrorController {
	
	public Logger Log = LogManager.getLogger(ApiErrorController.class) ;  
	
	@GetMapping("/api/error")
    public ResponseEntity<String> apierror(HttpServletRequest request, HttpServletResponse response) {
		Log.error("*** ApiError: {}", request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR")) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.BAD_REQUEST.getSttusCd())) ;
	}
	@GetMapping("/api/auth/error")
    public ResponseEntity<String> apiAuthError(HttpServletRequest request, HttpServletResponse response) {
		CommonMap vmap = (CommonMap)request.getAttribute("verifyMap") ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(vmap)) ;
	}
	
}
