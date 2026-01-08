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
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-501]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleException(NumberFormatException ex) {
    	ex.printStackTrace();
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("입력데이터 오류입니다.");
    	error.setHttpSttusText("[Error-502]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<?> handleException(ClassCastException ex) {
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	ex.printStackTrace();
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-503]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleException(NullPointerException ex) {
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-505]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> handleException(IndexOutOfBoundsException ex) {
    	ErrorInfoVo error = new ErrorInfoVo() ;
    	ex.printStackTrace(); 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-506]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }
    @ExceptionHandler(Exception.class) 
    public ResponseEntity<?> handleException(Exception ex) {
    	ErrorInfoVo error = new ErrorInfoVo() ; 
    	error.setErrorMessage("서버에서 에러가 발생하였습니다.");
    	error.setHttpSttusText("[Error-504]");
    	error.setHttpSttusCd(500);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getErrorJson(error)) ;
        
    }

}
