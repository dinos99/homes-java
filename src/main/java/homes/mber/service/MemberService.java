package homes.mber.service;

import java.sql.SQLException;

public interface MemberService {
	public String isExistUserByEmail( String email ) throws SQLException ; 
	public String isExistUserByCttpc( String mblecttpc ) throws SQLException ; 
}
