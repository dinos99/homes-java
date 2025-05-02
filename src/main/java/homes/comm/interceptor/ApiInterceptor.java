package homes.comm.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiInterceptor implements HandlerInterceptor {

	public static final Logger Log = LogManager.getLogger(ApiInterceptor.class) ;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	 
	 	Log.info("******************************************************************************");
	 	Log.info("*** Api interceptor: {}", request.getRequestURI()) ; 
	 	Log.info("******************************************************************************");
	    return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
