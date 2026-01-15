package homes.broker.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import homes.broker.mapper.BrokerMapper;
import homes.broker.vo.BrokerMemoVo;
import homes.broker.vo.BrokerOfficeVo;
import homes.broker.vo.BrokerVo;
import homes.comm.constants.EnumError;
import homes.comm.mapper.CommonMapper;
import homes.comm.util.HomesProperty;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommonMap;
import homes.comm.vo.FileVo;
import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrokerServiceImpl implements BrokerService {

	public final Logger Log = LogManager.getLogger(BrokerServiceImpl.class) ;

	private final CommonMapper commMapper ;
	private final BrokerMapper mapper ; 
	
	private String UPLOAD_BASE_PATH = "" ; 
	
	@Override
	@Transactional
	public FileVo brokerUploadfile(String path, Long brokerno, String prefix, MultipartFile file) throws SQLException {   	
		UPLOAD_BASE_PATH = HomesProperty.getPropVal("upload.base.path") ; 
        try {
            if (file.isEmpty()) {
                throw new HomesException(EnumError.FILE_NOT_FOUND.getSttusCd()) ;
            }

            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_BASE_PATH + path + File.separator + brokerno) ;
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Save the file to the server
            String f_name = file.getOriginalFilename() ;
            String ext    = f_name.substring(f_name.lastIndexOf(".") ) ;  
            
            f_name =  prefix + System.currentTimeMillis() + ext ; 
            Path filePath = uploadPath.resolve(f_name);
            Files.copy(file.getInputStream(), filePath);
            
            
            FileVo fvo = new FileVo() ;
            fvo.setUserno(brokerno);
            fvo.setFsvname(f_name);
            fvo.setForgname(file.getOriginalFilename());
            fvo.setFpath(path + File.separator + brokerno);
            if (prefix.indexOf("bix") > -1 ) {
                fvo.setFtype("FTY001");
            } else if ( prefix.indexOf("est") > -1 ) {
                fvo.setFtype("FTY002");
            } else {
            	fvo.setFtype("FTY999");
            }
            fvo.setFsize(file.getSize());
            
            commMapper.addfile(fvo) ;
            Long fileno = commMapper.getLastfileno(brokerno) ; 
            fvo.setFileno(fileno);
            return fvo ; 
        } catch (IOException e) {
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        }
	}

	@Transactional
	public long registbrokerOffice( BrokerOfficeVo paramVo) throws SQLException {
		return mapper.insertBrokerOffice(paramVo) ;
	}

	@Transactional
	public long registbroker( BrokerVo paramVo) throws SQLException {
		String existYn = Optional.ofNullable(mapper.isExistBroker(paramVo.getBrokerno())).orElse("N") ;
		if ( "N".equals( existYn )) {
			mapper.insertBrokerUser(paramVo) ;
		}
		return paramVo.getBrokerno() ; 
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class ) 
	public CommonMap insertMemo( HttpServletRequest request, BrokerMemoVo paramVo ) {
		
		BrokerVo bvo = RequestUtil.getBroker(request) ; 
		paramVo.setOfficeno(bvo.getOfficeno());
		paramVo.setBrkno(bvo.getBrokerno()); ; 
		
		int in_co = mapper.insertBrkMemo(paramVo) ; 
		CommonMap in_map = new CommonMap() ; 
		in_map.put("inco", in_co) ; 
		return in_map ; 
	}

	@Override
	@Transactional( readOnly = true ) 
	public List<BrokerMemoVo> selectMemoList( HttpServletRequest request, BrokerMemoVo paramVo ) {
		BrokerVo bvo = RequestUtil.getBroker(request) ; 
		paramVo.setOfficeno(bvo.getOfficeno());
		paramVo.setBrkno(bvo.getBrokerno()); ; 
		
		List<BrokerMemoVo> mList = mapper.selectBrokerMemo(paramVo) ;
		return mList ; 
	}

	@Override
	@Transactional( readOnly = true ) 
	public CommonMap selectMemoCount( HttpServletRequest request, BrokerMemoVo paramVo ) {
		BrokerVo bvo = RequestUtil.getBroker(request) ; 
		paramVo.setOfficeno(bvo.getOfficeno());
		paramVo.setBrkno(bvo.getBrokerno());
		CommonMap memo = mapper.selectBrokerMemoCount(paramVo) ;
		return memo ; 
	}
}
