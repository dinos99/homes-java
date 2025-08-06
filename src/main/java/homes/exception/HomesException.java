package homes.exception;

import homes.comm.constants.EnumError;

public class HomesException extends RuntimeException {
	private int code ; 
	private String title ; 
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
	public HomesException(int code, String message ) {
		this.code = code ; 
		this.message = message ; 
	}

	public HomesException(int code, String title, String message ) {
		this.code = code ; 
		this.title = title ; 
		this.message = message ; 
	}
	
	public int getCode() {
		return this.code ; 
	}
	
	public String getTitle() {
		return this.title ;
	}
	
	public String getMessage() {
		return this.message ;  
	}
	
}
