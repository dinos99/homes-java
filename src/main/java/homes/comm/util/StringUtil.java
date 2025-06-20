package homes.comm.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import homes.comm.vo.CommonMap;

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

	public static float getFloatValue(String fstr, float defVal ) throws NumberFormatException {
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
	
	public static int getIntValue( CommonMap cmap, String key, int defVal) {
		String s = Optional.ofNullable((String)cmap.get(key)).orElse(String.valueOf(defVal)) ; 
		return Integer.parseInt(s) ; 
	}

	public static int getIntValue( CommonMap cmap, String key) {
		return getIntValue(cmap, key, 0) ; 
	}

	public static int getIntValue(String istr ) {
		return getIntValue(istr, 0) ;
	}

	public static Long getLongValue(CommonMap cmap, String key, Long defVal ) {
		String s = Optional.ofNullable((String)cmap.get(key)).orElse(String.valueOf(defVal)) ; 
		return Long.parseLong(s) ; 		
	}

	public static Long getLongValue( CommonMap cmap, String key) {
		return getLongValue(cmap, key, 0l) ; 
	}
	
	public static Long getLongValue(Map<String, Object> cmap, String key, Long defVal ) {
		String s = Optional.ofNullable(String.valueOf(cmap.get(key))).orElse(String.valueOf(defVal)) ; 
		return Long.parseLong(s) ; 		
	}

	public static Long getLongValue( Map<String, Object> cmap, String key) {
		return getLongValue(cmap, key, 0l) ; 
	}	
	public static float getFloatValue(String fstr) throws NumberFormatException {
		return getFloatValue(fstr, 0.00f) ; 
	}

	public static long getLongValue(String lstr) {
		return getLongValue(lstr, 0l) ; 
	}
	
	public static String getCurrencyFormat( int iVal ) {
		DecimalFormat df = new DecimalFormat("#,###") ;
		String s = df.format(iVal) ;
		return s ; 
	}
	public static String getCurrencyFormat( double dVal ) {
		DecimalFormat df = new DecimalFormat("#,###.##") ;
		String s = df.format(dVal) ;
		return s ; 
	}
	
	public static String getStringValue( String val, String def ) {
		return Optional.ofNullable(val).orElse(def) ;  
	}
	
	public static String getStringValue( String val) {
		return getStringValue(val, "") ; 
	}
	
	public static String getStringValue(Map<String, Object> pMap, String key, String defVal) {
		if ( pMap == null ) {
			pMap = new HashMap<String, Object>() ;
			pMap.put(key, defVal) ;
		}
		return Optional.ofNullable(String.valueOf(pMap.get(key))).orElse(defVal) ;
	}

	public static String getStringValue(Map<String, Object> pMap, String key) {
		return getStringValue(pMap, key, "") ; 
	}
	public String getStringValue( CommonMap cmap, String key, String defVal) {
		String s = Optional.ofNullable((String)cmap.get(key)).orElse(defVal) ; 
		return s ; 
	}
	public String getStringValue( CommonMap cmap, String key ) {
		return getStringValue(cmap, key, "") ; 
	}
}
