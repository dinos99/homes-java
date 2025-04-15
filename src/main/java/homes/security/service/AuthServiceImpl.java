package homes.security.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.constants.EnumError;
import homes.comm.util.JwtUtil;
import homes.comm.vo.AccessTokenVo;
import homes.exception.HomesException;
import homes.security.dto.HomesUserInfoDto;
import homes.security.mapper.CommUserMapper;
import homes.security.vo.CommUserVo;
import homes.security.vo.LoginRequestVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

	private final Logger Log = LogManager.getLogger(AuthServiceImpl.class) ; 
	
    private final JwtUtil jwtUtil;
//    private final PasswordEncoder encoder  ; 
    
    private final CommUserMapper commUserMapper ;  

    @Override
    @Transactional
    public AccessTokenVo Login(LoginRequestVo paramVo) {

        String password = paramVo.getPassword();
        Log.info("*** user password: {}", password) ;
        
        try {
            password = URLDecoder.decode(new String(Base64.getDecoder().decode(password.getBytes()), "utf-8"), "utf-8") ;
        } catch  ( IllegalArgumentException | UnsupportedEncodingException e ) {
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ; 
        }
        
        paramVo.setPassword(password);
             
        CommUserVo commUserVo = commUserMapper.findCommUserByEmail(paramVo) ; 
        if(commUserVo == null) {
        	throw new HomesException(EnumError.USER_NOT_FOUND.getSttusCd()) ; 
        }
        
        HomesUserInfoDto.builder()
        				.userNo(commUserVo.getUserNo())
        				.email(commUserVo.getEmail())
        				.name(commUserVo.getUserNm())
        				.role(commUserVo.getUserRole())
        				.build() ; 
        
        String accessToken = jwtUtil.createAccessToken(HomesUserInfoDto.builder()
				.userNo(commUserVo.getUserNo())
				.email(commUserVo.getEmail())
				.name(commUserVo.getUserNm())
				.role(commUserVo.getUserRole())
				.build());
        
        AccessTokenVo tokenVo = jwtUtil.getTokenInfo(accessToken) ;
        tokenVo.setUserNm(commUserVo.getUserNm());
        return tokenVo ;
    }
    
    
}
