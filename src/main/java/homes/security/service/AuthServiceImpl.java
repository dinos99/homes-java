package homes.security.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.util.JwtUtil;
import homes.comm.vo.AccessTokenVo;
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

    	// 출처: https://sjh9708.tistory.com/170 [데굴데굴 개발자의 기록:티스토리]
    	
    	String email    = paramVo.getEmail();
        String password = paramVo.getPassword();
        
        Log.info("*** user email   : {}", email) ; 
        Log.info("*** user password: {}", password) ; 
        
        CommUserVo commUserVo = commUserMapper.findCommUserByEmail(paramVo) ; 
        if(commUserVo == null) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
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
