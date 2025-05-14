package homes.comm.util;

import java.util.Optional;

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
		
//		Log.info("*** origin name: {}, converted name: {}", name, result.toString()) ; 
		return result.toString() ;   
	}

	public static float getFloatValue(String fstr, float defVal ) {
		String floatStr = Optional.ofNullable(fstr).orElse("") ; 
		floatStr = "".equals(floatStr) ? String.valueOf(defVal) : floatStr ;
		return Float.parseFloat(floatStr) ; 
	}
	
	public static long getLongValue(String lstr, long defVal ) {
		String longStr = Optional.ofNullable(lstr).orElse("") ; 
		longStr = "".equals(longStr) ? String.valueOf(defVal) : longStr ;
		return Long.parseLong(longStr) ; 
		
	}

	public static int getIntValue(String istr, long defVal ) {
		String intStr = Optional.ofNullable(istr).orElse("") ; 
		intStr = "".equals(intStr) ? String.valueOf(defVal) : intStr ;
		return Integer.parseInt(intStr) ;  
		
	}
	
	public static float getFloatValue(String fstr) {
		return getFloatValue(fstr, 0.00f) ; 
	}

	public static long getLongValue(String lstr) {
		return getLongValue(lstr, 0l) ; 
	}

	public static int getIntValue(String istr ) {
		return getIntValue(istr, 0) ;
	}
}
