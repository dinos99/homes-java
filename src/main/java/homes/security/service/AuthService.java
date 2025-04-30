package homes.security.service;

import homes.comm.vo.AccessTokenVo;
import homes.security.vo.CommUserVo;
import homes.security.vo.LoginRequestVo;

public interface AuthService {
    public AccessTokenVo Login(LoginRequestVo paramVo) ; 
    public CommUserVo slectUserInfo(Long userno) ; 
}
