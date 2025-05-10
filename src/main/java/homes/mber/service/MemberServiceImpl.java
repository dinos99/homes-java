package homes.mber.service;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.security.mapper.CommUserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
	public Logger Log = LogManager.getLogger(MemberServiceImpl.class) ; 
	
	private final CommUserMapper mapper ;
	
	@Transactional(readOnly = true)
	public String isExistUserByEmail(String email) throws SQLException {
		return mapper.isExistUserByEmail(email) ;
	}

	@Transactional(readOnly = true)
	public String isExistUserByCttpc( String mblecttpc ) throws SQLException {
		return mapper.isExistUserByCttpc(mblecttpc) ;
	}

}
