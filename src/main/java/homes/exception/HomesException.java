package homes.exception;

import homes.comm.constants.EnumError;

public class HomesException extends RuntimeException {

	private int code ; 
	private String message ; 
	
	public HomesException () {
		this.code = EnumError.BAD_REQUEST.getSttusCd() ; 
		this.message = EnumError.BAD_REQUEST.getMessage() ; 
	}

	public HomesException(String errorMessage ) {
		EnumError sttus = EnumError.getStatusFromCode(EnumError.BAD_REQUEST.getSttusCd()) ;
		this.code = sttus.getSttusCd() ; 
		this.message = errorMessage ; 
	}
	
	public HomesException(int code ) {
		EnumError sttus = EnumError.getStatusFromCode(code) ;
		this.code = sttus.getSttusCd() ; 
		this.message = sttus.getMessage() ; 
	}
	
	public int getCode() {
		return this.code ; 
	}
	
	public String getMessage() {
		return this.message ;  
	}
	
}
