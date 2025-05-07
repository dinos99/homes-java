package homes.comm.constants;

public enum EnumError {

    HTTP_OK(200, "HTTP_OK"),
	BAD_REQUEST(400, "잘못된 요청입니다."),
    UNAUTHORIZED(401, "사용자가 인증되지 않았습니다."),
    FORBIDDEN(403, "페이지를 찾을 수 없습니다."),
	NOT_FOUND(404, "페이지를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(500, "서버에서 에러가 발생하였습니다."), 

	/* Custom Exception 900 ~ 999 */
	USER_NOT_FOUND(900, "존재하지 않는 사용자입니다."),
    INVALID_TOKEN(901, "사용자인증에 실패하였습니다."),
    UNSUPPORTED_TOKEN(902, "사용자인증에 실패하였습니다."),
    TOKEN_EXPIRED(904, "만로된 토큰입니다."),

	FILE_NOT_FOUND(910, "파일을 찾을 수 없습니다.")
	;
	

    private final int sttusCd ;
    private final String sttusMsg ;
    
    EnumError(int sttusCd, String sttusMsg ) {
    	this.sttusCd = sttusCd ; 
    	this.sttusMsg = sttusMsg ;
    }
    
    public int getSttusCd() {
    	return sttusCd ; 
    }
    
    public String getMessage() {
    	return sttusMsg ; 
    }

    public static EnumError getStatusFromCode(int sttusCd) {
        for (EnumError status : EnumError.values()) {
            if (status.getSttusCd() == sttusCd) {
                return status;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }


	
}
