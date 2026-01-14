package homes.comm.util;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import homes.broker.vo.BrokerVo;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
	
	public static Logger Log = LogManager.getLogger(RequestUtil.class) ;
	
	public static int getUserno(HttpServletRequest request) {
		Long userno = (Long)request.getAttribute("userno") ; 
		return (int) userno.longValue() ;  
	}
	
	public static BrokerVo getBroker(HttpServletRequest request) {
		return new BrokerVo() ; 
	}
	
	public static String getToken(HttpServletRequest request) {
		return Optional.ofNullable((String)request.getAttribute("accessToken")).orElse("") ; 
	}
	
}
