package homes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import homes.comm.interceptor.ApiAuthInterceptor;
import lombok.RequiredArgsConstructor;
 

@Configuration
/* @EnableWebMvc */ 
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer   {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiAuthInterceptor())
//        registry.addInterceptor(jwtTokenInterceptor)
        	.addPathPatterns("/**") 
        	.excludePathPatterns(
        			"/favicon.ico",
        			"/api/error",
        			"/auth/sign-in"); 
    }
    

    /**
     *  이 배열은 정적 리소스가 위치할 수 있는 경로를 나열하고 있다. 이 경로들은 addResourceHandlers 메서드에서 사용된다.
     *  wab/was 분리되지 않은 경우 사용 
     */
/*
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/",
            "classpath:/resources/",
            "classpath:/META-INF/resources/",
            "classpath:/META-INF/resources/webjars/"
    };
*/
    
    /**
     * 뷰 컨트롤러를 추가할때 사용된다. 여기서는 루트 URL("/")에 접근했을 때 "/login"으로 리다이렉트하도록 설정하고 있다.
*/
    /*
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
*/
    

    /**
     * 정적 리소스를 처리하는 핸들러를 추가하는데 사용된다.
     * 여기서는 모든 요청("/**")에 대해 CLASSPATH_RESOURCE_LOCATIONS에 지정된 경로에서 리소스를 찾도록 설정하고 있다.
     */
/* 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
*/
    
}
