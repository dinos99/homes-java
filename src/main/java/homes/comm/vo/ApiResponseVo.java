package homes.comm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class ApiResponseVo {
	
	@JsonProperty("token")
	private AccessTokenVo tokenVo ;

	@JsonProperty("error")
	private ErrorInfoVo   errorVo ;
	

	@JsonProperty("data")
	private Object responseVo ; 
	
}
