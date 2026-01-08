package homes.comm.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import homes.comm.constants.EnumError;
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

	public static String getErrorJson(ErrorInfoVo errorVo) {
		String json = "" ;
    	Map<String, Object> emap = new HashMap<String, Object>() ; 
    	emap.put("error", errorVo) ;
        ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(emap);
		} catch (JsonProcessingException e) {
			Log.error("*** Json parsing Error") ; 
			json = getErrorJsonStr() ; 
		}
		
		return json ; 
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
        errorVo.setHttpSttusCd(EnumError.HTTP_OK.getSttusCd()) ; 
        errorVo.setErrorMessage(EnumError.HTTP_OK.getMessage()) ;
        
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
	public static String getJson( Object resultVo ) {
		String json = "" ; 

        ErrorInfoVo errorVo = new ErrorInfoVo() ; 
        errorVo.setHttpSttusCd(EnumError.HTTP_OK.getSttusCd()) ; 
        errorVo.setErrorMessage(EnumError.HTTP_OK.getMessage()) ;
        
		ApiResponseVo respVo = new ApiResponseVo() ;
		respVo.setErrorVo(errorVo);
		respVo.setTokenVo(null);
		respVo.setResponseVo(resultVo);
		
        ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(respVo);
		} catch (JsonProcessingException e) {
			Log.error("*** Json parsing error: {}", e.getMessage()) ; 
			json = getErrorJsonStr() ; 
		}
		
		return json ; 
		
	}
	
	public static String getJson(int code, Object resultVo) {
		String json = "" ; 

		EnumError error = EnumError.getStatusFromCode(code) ;
		ErrorInfoVo errorVo = new ErrorInfoVo() ;  
		errorVo.setHttpSttusCd(error.getSttusCd());
		errorVo.setErrorMessage(error.getMessage());
		ApiResponseVo respVo = new ApiResponseVo() ;
		respVo.setErrorVo(errorVo);
		respVo.setTokenVo(null);
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
	
	public static String getJson(int code) {
		String json = "" ; 

		EnumError error = EnumError.getStatusFromCode(code) ;
		ErrorInfoVo errorVo = new ErrorInfoVo() ;  
		errorVo.setHttpSttusCd(error.getSttusCd());
		errorVo.setErrorMessage(error.getMessage());
		ApiResponseVo respVo = new ApiResponseVo() ;
		respVo.setErrorVo(errorVo);
		respVo.setTokenVo(null);
		respVo.setResponseVo(null);
		
        ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(respVo);
		} catch (JsonProcessingException e) {
			Log.error("*** Json parsing ") ; 
			json = getErrorJsonStr() ; 
		}
		
		return json ; 
	}
	
	public static String convString( String key, String str_json ) {

        // 2. 정규식 패턴 설정
        // 설명: mgmBldrgstPk 뒤의 콜론(:) 다음에 오는 숫자들(\\d+)을 찾아서 그룹화합니다.
//        String regex = "\"mgmBldrgstPk\"\\s*:\\s*(\\d+)";
        String regex = "\"" + key + "\"\\s*:\\s*(\\d+)" ; 
//        System.out.println("regexp: " + regex) ; 
        // 3. 변환 실행 ($1은 첫 번째 괄호에서 찾은 숫자 그룹을 의미함)
        String replacement = "\"mgmBldrgstPk\":\"$1\"";
        String convStr     = str_json.replaceAll(regex, replacement);

        // 4. 결과 출력
//        System.out.println("변환 전: " + str_json);
//        System.out.println("변환 후: " + convStr);
        
        return convStr ; 
	}
}
