package homes.comm.interceptor;

import java.util.Enumeration;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import homes.comm.constants.EnumError;
import homes.comm.util.JwtUtil;
import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiAuthInterceptor implements HandlerInterceptor {

	public static final Logger Log = LogManager.getLogger(ApiAuthInterceptor.class) ;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	 
	 	Enumeration<String> enumHeader = request.getHeaderNames() ;
	 	boolean is_auth = false ; 
	 	Log.info("******************************************************************************");
	 	Log.info("*** Api Auth interceptor: {}", request.getRequestURI()) ; 

	 	JwtUtil jwtUtil = new JwtUtil() ; 
	 	while(enumHeader.hasMoreElements()) {
	 		String header_name = enumHeader.nextElement() ;
	 		if ( "Authorization".equalsIgnoreCase(header_name)) {
	 			String accessToken = request.getHeader("Authorization") ; 
	 			String token = Optional.ofNullable(accessToken).orElse("").replaceFirst("Bearer ", "") ;
	 		 	Log.info("*** Authorization: {}, token : {}", token) ;
	 			is_auth = jwtUtil.validateToken(token) ;
	 		 	Log.info("*** Authorization: {}, isAuth: {}", accessToken, is_auth) ;
	 			break ;
	 		}
	 		Log.info("*** \t {} :{}", header_name, request.getHeader(header_name)) ;  
	 	}
	 	Log.info("******************************************************************************");
	 	
	 	if (!is_auth) {
	 		throw new HomesException(EnumError.UNSUPPORTED_TOKEN.getSttusCd()) ; 
	 	}
	 	
	    return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
