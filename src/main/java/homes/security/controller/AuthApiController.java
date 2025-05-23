package homes.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.util.JsonUtil;
import homes.comm.vo.AccessTokenVo;
import homes.exception.HomesException;
import homes.security.service.AuthService;
import homes.security.vo.CommUserVo;
import homes.security.vo.LoginRequestVo;
import io.jsonwebtoken.io.Encoders;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/auth/homesLogin")
    public ResponseEntity<String> getMemberProfile(@RequestBody LoginRequestVo request) {
        AccessTokenVo tokenVo = this.authService.Login(request);
    	CommUserVo    userVo  = this.authService.slectUserInfo(tokenVo.getUserno());
    	String mbno = Encoders.BASE64.encode(userVo.getMobileno().getBytes()) ;
    	userVo.setMobileno(mbno) ; 
    	userVo.setAccessToken(tokenVo.getAccessToken()) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(tokenVo, userVo));
    }
    

    @PostMapping("/auth/sign-in")
    public ResponseEntity<String> getManagerSignIn(@RequestBody LoginRequestVo request) {
    	AccessTokenVo tokenVo = null ; 
    	CommUserVo    userVo  = null ; 
    	try {
            tokenVo = this.authService.Login(request);
        	userVo  = this.authService.slectUserInfo(tokenVo.getUserno());
        	userVo.setAccessToken(tokenVo.getAccessToken()) ; 
    	} catch ( HomesException he ) {
            return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(he.getCode(), null)); 
    		
    	}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(tokenVo, userVo));
    }
    
    @PostMapping("/api/v1/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestBody LoginRequestVo request) {
    	AccessTokenVo tokenVo = null ; 
    	try {
            tokenVo = this.authService.Login(request);
    	} catch ( HomesException he ) {
            return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(he.getCode(), null)); 
    		
    	}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(tokenVo, tokenVo));
    }
}
