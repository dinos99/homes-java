package homes.security.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

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
import homes.security.vo.UpdateLoginInfoReqVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

//	private final Logger Log = LogManager.getLogger(AuthServiceImpl.class) ; 
	
    private final JwtUtil jwtUtil;
//    private final PasswordEncoder encoder  ; 
    
    private final CommUserMapper commUserMapper ;  

    @Override
    @Transactional
    public AccessTokenVo Login(LoginRequestVo paramVo) {

        String password = paramVo.getPassword();
//        Log.info("*** user password: {}", password) ;
        
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
        				.userno(commUserVo.getUserno())
        				.email(commUserVo.getEmail())
        				.name(commUserVo.getUsernm())
        				.arcode(commUserVo.getArcode())
        				.role(commUserVo.getUserRole())
        				.build() ; 
        
        String accessToken = jwtUtil.createAccessToken(HomesUserInfoDto.builder()
				.userno(commUserVo.getUserno())
				.email(commUserVo.getEmail())
				.name(commUserVo.getUsernm())
				.role(commUserVo.getUserRole())
				.build());

        /* 사용자 정보 ( 최종로그인시간, accessToken 업데이트 ) */
        UpdateLoginInfoReqVo upVo = new UpdateLoginInfoReqVo() ; 
        upVo.setUserNo(commUserVo.getUserno());
        upVo.setAccessToken(accessToken);
    	commUserMapper.updateUserLogininfo(upVo) ;
        /* 사용자 로그인이력 추가할것 */ 
    	
        AccessTokenVo tokenVo = jwtUtil.getTokenInfo(accessToken) ;
        tokenVo.setUsernm(commUserVo.getUsernm());
        return tokenVo ;
    }

	@Override
	@Transactional(readOnly = true)
	public CommUserVo slectUserInfo(Long userno) {
        return commUserMapper.findCommUserByUserNo(userno) ;
	}
        
}
