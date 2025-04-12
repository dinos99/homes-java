package homes.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.util.JsonUtil;
import homes.comm.vo.AccessTokenVo;
import homes.security.service.AuthService;
import homes.security.vo.LoginRequestVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/v1/auth/homesLogin")
    public ResponseEntity<String> getMemberProfile(@RequestBody LoginRequestVo request) {
        AccessTokenVo tokenVo = this.authService.Login(request);
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(tokenVo, tokenVo));
    }
}
