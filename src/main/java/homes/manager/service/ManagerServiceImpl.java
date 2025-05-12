package homes.manager.service;

import java.sql.SQLException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.constants.EnumError;
import homes.comm.vo.CommUserReqVo;
import homes.exception.HomesException;
import homes.manager.mapper.ManagerMapper;
import homes.manager.vo.ManagerVo;
import homes.security.mapper.CommUserMapper;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {
	public final Logger Log = LogManager.getLogger(ManagerServiceImpl.class) ;

	private final CommUserMapper commUserMapper ;
	private final ManagerMapper mapper ;

	@Override
	@Transactional
	public ManagerVo registmanager(ManagerVo paramVo) throws SQLException {
		String existYn = Optional.ofNullable(commUserMapper.isExistUserByEmail(paramVo.getEmail())).orElse("N") ;
		if ( "Y".equals(existYn)) {
			throw new HomesException(EnumError.IS_EXIST_EMAIL.getSttusCd()) ; 
		}
		
		/* 공통사용자 등록 */ 
		CommUserReqVo userVo = new CommUserReqVo();
		userVo.setEmail(paramVo.getEmail());
		userVo.setPassword("homes1234") ; /* 기본비밀번호 */ 
		userVo.setUsernm(paramVo.getManagernm());
		userVo.setUserRole("MBT004"); /* 관리자 */
		userVo.setUserSttus("CTF003") ; /* 일단은 기본 승인완료 */ 
		userVo.setLoginTy("LGT001"); /* email 로그인 */ 
		commUserMapper.insertCommuser(userVo) ; 
		Long managerno = commUserMapper.getLastUserno(userVo) ;
		
		/* 관리자 등록 시작 */
		String empno = mapper.getEmpno("M-") ; 
		paramVo.setEmpno(empno);
		paramVo.setManagerno(managerno);
		mapper.insertManager(paramVo) ;
		return paramVo ;
	}

}
