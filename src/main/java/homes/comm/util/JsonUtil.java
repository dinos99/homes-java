package homes.comm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import homes.comm.vo.AccessTokenVo;
import homes.comm.vo.ApiResponseVo;
import homes.comm.vo.ErrorInfoVo;

public class JsonUtil {
	 
	public static final Logger Log = LogManager.getLogger(JsonUtil.class) ; 
	
	private static final String _CRLF  = "\r\n" ; 
	private static final String _TAB   = "\t" ;
	private static final String _COLON = ":" ;
	private static final String _COMMA = "," ; 
	private static final String _QUOTATION   = "\"" ; 
	private static final String _BRACE_OPEN  = "{" ; 
	private static final String _BRACE_CLOSE = "}" ; 
	
	public static String getErrorJsonStr() {
		StringBuffer sb = new StringBuffer() ;
		return  sb.append(_BRACE_OPEN).append(_CRLF)
				  .append(_TAB).append(_QUOTATION).append("error").append(_QUOTATION).append(_COLON).append(_BRACE_OPEN).append(_CRLF)
				  .append(_TAB).append(_TAB).append(_QUOTATION).append("HttpSttusCd").append(_QUOTATION).append(_COLON).append( HttpStatus.INTERNAL_SERVER_ERROR.value())
				  .append(_TAB).append(_TAB).append(_COMMA).append(_QUOTATION).append("httpSttusCdnm").append(_QUOTATION).append(_COLON).append(_QUOTATION).append( HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).append(_QUOTATION)
				  .append(_TAB).append(_TAB).append(_COMMA).append(_QUOTATION).append("errorMessage").append(_QUOTATION).append(_COLON).append(_QUOTATION).append( HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).append(_QUOTATION)
				  .append(_TAB).append(_BRACE_CLOSE).append(_CRLF)
				  .append(_BRACE_CLOSE).toString() ;
	}
	
	public static String getJson(AccessTokenVo tokenVo, ErrorInfoVo errorVo, Object resultVo ) {
		String json = "" ; 
		
		ApiResponseVo respVo = new ApiResponseVo() ;
		respVo.setErrorVo(errorVo);
		respVo.setTokenVo(tokenVo);
		respVo.setResponseVo(resultVo);
		
        ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(respVo);
		} catch (JsonProcessingException e) {
			Log.error("*** Json parsing ") ; 
			json = getErrorJsonStr() ; 
		}
		
		return json ; 
		
	}
	

	public static String getJson(AccessTokenVo tokenVo, Object resultVo ) {
		String json = "" ; 

        ErrorInfoVo errorVo = new ErrorInfoVo() ; 
        errorVo.setHttpSttusCd(HttpStatus.OK.value()) ;
        errorVo.setHttpSttusCdnm(HttpStatus.OK.getReasonPhrase()) ; 
        errorVo.setErrorMessage("OK") ; 
        
		ApiResponseVo respVo = new ApiResponseVo() ;
		respVo.setErrorVo(errorVo);
		respVo.setTokenVo(tokenVo);
		respVo.setResponseVo(resultVo);
		
        ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(respVo);
		} catch (JsonProcessingException e) {
			Log.error("*** Json parsing ") ; 
			json = getErrorJsonStr() ; 
		}
		
		return json ; 
		
	}
	
}
