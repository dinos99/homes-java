package homes.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfig  {

	@Autowired
	private DataSource dataSource; // application.properties에 설정한 spring.datasource D.I

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).httpBasic(hbasic -> hbasic.disable())
				.headers(config -> config.frameOptions(customizer -> customizer.sameOrigin()))
				.authorizeHttpRequests(
						authz -> authz.anyRequest().permitAll())
				.sessionManagement(session -> session.maximumSessions(1))
				.build();
	}
}
