package homes.config;

import java.util.Arrays;
import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfig  {

	@Autowired
	private DataSource dataSource; // application.properties에 설정한 spring.datasource D.I

	// ⭐️ CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Arrays.asList(
            		"http://127.0.0.1:8080",
            		"http://127.0.0.1:8081",
            		"http://127.0.0.1:8082"
            )); // ⭐️ 허용할 origin
//            config.setAllowedOriginPatterns(Collections.singletonList("http://127.0.0.1:8082")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }
    
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(c -> c.configurationSource(corsConfigurationSource()))
        	.csrf(c -> c.disable())
        	.authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
        ;
		return http.build();
	}
}
