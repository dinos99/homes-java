package homes.broker.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import homes.broker.vo.BrokerMemoVo;
import homes.broker.vo.BrokerOfficeVo;
import homes.broker.vo.BrokerVo;
import homes.comm.vo.CommonMap;
import homes.comm.vo.FileVo;
import jakarta.servlet.http.HttpServletRequest;

public interface BrokerService {
	public FileVo brokerUploadfile (String path, Long brokerno, String prefix, MultipartFile file) throws SQLException ;
	public long registbrokerOffice( BrokerOfficeVo paramVo) throws SQLException ; 
	public long registbroker( BrokerVo paramVo) throws SQLException ;
	
	
	public CommonMap insertMemo( HttpServletRequest request, BrokerMemoVo paramVo ) ;	
	public CommonMap selectMemoCount( HttpServletRequest request, BrokerMemoVo paramVo ) ;	
	public List<BrokerMemoVo> selectMemoList( HttpServletRequest request, BrokerMemoVo paramVo ) ; 
}
