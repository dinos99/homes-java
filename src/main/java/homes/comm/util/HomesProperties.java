package homes.comm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class HomesProperties {
	@Value("${jwt.key.secret}")
	private String secretKey ;
	@Value("${jwt.expiration_time}") 
	private Long   expTime ;	
}
