package homes.comm.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggerInterceptor implements HandlerInterceptor {

	public static final Logger Log = LogManager.getLogger(LoggerInterceptor.class) ;

	 @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		 	Log.info("******************************************************************************");
		 	Log.info("Request URI: {}", request.getRequestURI());
	        return HandlerInterceptor.super.preHandle(request, response, handler);
	    }

	    @Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		 	Log.info("******************************************************************************");
	        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	    }
}
