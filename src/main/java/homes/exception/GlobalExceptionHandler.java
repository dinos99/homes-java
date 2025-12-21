package homes.exception;

import java.sql.SQLException;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import homes.comm.util.JsonUtil;
import homes.comm.vo.ErrorInfoVo;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HomesException.class)
    public ResponseEntity<?> handleHomesException(HomesException ex) {
    	
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage(ex.getMessage());
    	error.setHttpSttusText(ex.getTitle());
    	error.setHttpSttusCd(ex.getCode());
    	
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    
    @ExceptionHandler(MyBatisSystemException.class) 
    public ResponseEntity<?> handleMyBatisException(MyBatisSystemException ex) {
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[MBError-9999]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSQLException(SQLException ex) {
    	
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	/*
    	error.setErrorMessage(ex.getMessage());
    	error.setHttpSttusText("SQLError-" + ex.getErrorCode());
    	error.setHttpSttusCd(ex.getErrorCode());
    	*/
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-" + ex.getErrorCode() + "]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    
    @ExceptionHandler(NoClassDefFoundError.class)
    public ResponseEntity<?> handleException(NoClassDefFoundError ex) {
    	ex.printStackTrace();
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-500]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    
    @ExceptionHandler(Exception.class) 
    public ResponseEntity<?> handleException(Exception ex) {
    	
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-500]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
}
