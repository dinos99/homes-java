package homes.system.service;

import java.sql.SQLException;
import java.util.List;

import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.system.vo.DomainListVo;
import homes.system.vo.DomainVo;
import homes.system.vo.SystemReqVo;

public interface SystemService {
	CommResponseVo domainList(SystemReqVo reqVo) throws SQLException ;
	List<CommonMap> selectExistsDomainList( DomainListVo dmListVo ) throws SQLException ; 
	int modifyDomain( DomainVo paramVo ) throws SQLException ;
}
