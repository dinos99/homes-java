package homes.comm.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/error")
public class BasicErrorController extends AbstractErrorController {
	
	public BasicErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	public Logger Log = LogManager.getLogger(ApiErrorController.class) ;  
	
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

		Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.defaults());
		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(body, status);
	}    
}
