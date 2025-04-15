package homes.config;

import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
            config.setAllowedOriginPatterns(Collections.singletonList("http://127.0.0.1:8080")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }
    
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(c -> c.configurationSource(corsConfigurationSource()))
        	.csrf(c -> c.disable())
        	.authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher("/comments/**"), 
                    AntPathRequestMatcher.antMatcher("/requests/admin/**")
                ).authenticated()
                .anyRequest().permitAll())
//        		.authenticationProvider(authenticationProvider())
//        		.exceptionHandling(r -> r.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//        		.sessionManagement(r -> r.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        		// Add a filter to validate the tokens with every request
//        		.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        ;
		return http.build();
		/*
		return http.csrf(csrf -> csrf.disable()).httpBasic(hbasic -> hbasic.disable())
				.headers(config -> config.frameOptions(customizer -> customizer.sameOrigin()))
				.authorizeHttpRequests(
						authz -> authz.anyRequest().permitAll())
				.build();
		*/
	}
}
