package homes.comm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class StringUtil extends JdbcUtils {
	
	public static Logger Log = LogManager.getLogger(StringUtil.class) ;
	
	public static String convCamelCase(@Nullable String name ) {
		if (!StringUtils.hasLength(name)) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		
		/* *********************************************************************
		 * undersore( _ )가 없다면 판단을 시작한다.
		 * 	모두 대문자면 모두 소문자로 변환
		 * 	대소문자 섞여있으면 원문자열 그대로 반환
		 * *********************************************************************/ 
		if ( name.indexOf("_") < 0 ) {
			String keyname = name ; 
			boolean is_upper = keyname.matches("[A-Z0-9]+") ; 
			return is_upper ? keyname.toLowerCase() : keyname ; 
		}
		
		boolean nextIsUpper = false;
		if (name.length() > 1 && name.charAt(1) == '_') {
			result.append(Character.toUpperCase(name.charAt(0)));
		}
		else {
			/* 대소문자 구분없이 그냥 원래문자를 반환 */ 
//			result.append(name.charAt(0));
			result.append(Character.toLowerCase(name.charAt(0)));
		}
		for (int i = 1; i < name.length(); i++) {
			char c = name.charAt(i);
			if (c == '_') {
				nextIsUpper = true;
			}
			else {
				if (nextIsUpper) {
					result.append(Character.toUpperCase(c));
					nextIsUpper = false;
				}
				else {
					result.append(Character.toLowerCase(c));
				}
			}
		}
		
		Log.info("*** origin name: {}, converted name: {}", name, result.toString()) ; 
		return result.toString() ;   
	}
}
