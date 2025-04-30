package homes.comm.exception;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SpringMVCException implements HandlerExceptionResolver {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
 
        try {
            if (ex instanceof HomesException) {
                String accept = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_OK);
 
                if (accept.equals("application/json")) {
                    HashMap<String, Object> errorResult = new HashMap<>();
                    errorResult.put("message", ex.getMessage());
 
                    String result = objectMapper.writeValueAsString(errorResult);
 
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result);
 
                    return new ModelAndView();
                } else {
                    return new ModelAndView("/error/500");
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
 
        return null;
    }
    
}
