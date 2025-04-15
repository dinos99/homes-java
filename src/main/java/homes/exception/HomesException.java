package homes.exception;

import homes.comm.constants.EnumError;

public class HomesException extends RuntimeException {

	private int code ; 
	private String message ; 
	
	public HomesException () {
		this.code = EnumError.INTERNAL_SERVER_ERROR.getSttusCd() ; 
		this.message = EnumError.INTERNAL_SERVER_ERROR.getMessage() ; 
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
