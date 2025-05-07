package homes.broker.service;

import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import homes.broker.vo.BrokerOfficeVo;
import homes.comm.vo.FileVo;

public interface BrokerService {

	public FileVo brokerUploadfile (String path, Long brokerno, String prefix, MultipartFile file) throws SQLException ;
	public long registbroker( BrokerOfficeVo paramVo) throws SQLException ; 
	
}
