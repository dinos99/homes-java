package homes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import homes.comm.util.JsonUtil;
import homes.comm.vo.ErrorInfoVo;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HomesException.class)
    public ResponseEntity<?> handleDa9Exception(HomesException ex) {
    	
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage(ex.getMessage());
    	error.setHttpSttusText(ex.getTitle());
    	error.setHttpSttusCd(ex.getCode());
    	
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
}
