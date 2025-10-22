package homes.comm.util;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
	
	public static Logger Log = LogManager.getLogger(RequestUtil.class) ;
	
	public static Long getUserno(HttpServletRequest request) {
		Long userno = Long.valueOf((long)request.getAttribute("userno")) ; 
		return userno ; 
	}
	
	public static String getToken(HttpServletRequest request) {
		return Optional.ofNullable((String)request.getAttribute("accessToken")).orElse("") ; 
	}
	
}
