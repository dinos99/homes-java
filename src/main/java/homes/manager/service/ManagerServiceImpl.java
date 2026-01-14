package homes.manager.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

import homes.comm.constants.EnumError;
import homes.comm.util.HomesProperty;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.manager.mapper.ManagerMapper;
import homes.manager.vo.ManagerVo;
import homes.manager.vo.TodoVo;
import homes.security.mapper.CommUserMapper;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {
	public final Logger Log = LogManager.getLogger(ManagerServiceImpl.class) ;

	private final CommUserMapper commUserMapper ;
	private final ManagerMapper mapper ;

	private final String UPLOAD_BASE_PATH = HomesProperty.getStringVal("batch.job.base.path")  ; 
	
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
	
	@Override
	public int  uploadBuildFile( String fType, MultipartFile file) {

		FileReader fr     = null ;
		BufferedReader br = null ; 

		FileWriter fw  = null ; 
		PrintWriter wr = null ;
		
		int fileidx = 0 ;
		
        if (file.isEmpty()) {
            throw new HomesException(EnumError.FILE_NOT_FOUND.getSttusCd()) ;
        }
        
        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_BASE_PATH + File.separator + fType) ;
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Save the file to the server
            String f_name = file.getOriginalFilename() ;
            String ext    = f_name.substring(f_name.lastIndexOf(".")) ;  
            
            String timesamp = String.valueOf(System.currentTimeMillis()) ; 
            // 일단 1000개만 잘라보자
            f_name =  fType + "-" + timesamp + "-origin" +  ext ; 
            Path filePath = uploadPath.resolve(f_name);
            Files.copy(file.getInputStream(), filePath);
            
            /* Upload file 읽기 */ 
			fileidx      = 1 ;
			Long lineno       = 1l ; 
            String sp_file_nm = "" ; 

            File up_file = new File(UPLOAD_BASE_PATH + File.separator + fType + File.separator + f_name) ;
			fr = new FileReader(up_file) ;
			br = new BufferedReader(fr) ;
			
			String line = "" ;
			while((line = br.readLine()) != null ) {
				if (( lineno % 1000 ) == 1 ) {
					 
					if ( fw != null ) fw.close() ;
					if ( wr != null ) wr.close();
					
					sp_file_nm = fType + "-" + timesamp + "-" + (fileidx * 1000) + ext ;
		            File sp_file = new File(UPLOAD_BASE_PATH + File.separator + fType + File.separator + sp_file_nm) ;
					fw = new FileWriter(sp_file);             
					wr = new PrintWriter(fw);
					
					fileidx ++ ; 
				}
				 
				wr.println(line);
				lineno ++ ;
			}
			
			fileidx = fileidx - 1 ;
			/* move origin file to done */ 
			File mvpath = new File(UPLOAD_BASE_PATH + File.separator + "/done") ; 
			if ( !mvpath.isDirectory()) {
				mvpath.mkdirs() ; 
			}			
			
			if ( wr != null ) wr.close();  
			if ( fw != null ) fw.close();  
			
			if ( br != null ) br.close() ;
			if ( fr != null ) fr.close()  ;             

			File src  = new File( UPLOAD_BASE_PATH + File.separator + fType + File.separator + f_name) ; 
			File dest = new File( mvpath.getAbsolutePath() + File.separator + "done-" + f_name ) ;  
			
			Log.info(UPLOAD_BASE_PATH + File.separator + fType + File.separator + f_name) ;
			Log.info(mvpath.getAbsolutePath() + File.separator + "done-" + f_name) ;
			src.renameTo(dest) ;
        } catch ( IOException e ) {
        	Log.error("*** Batchfile Upload Error: {}:", e) ; 
        	throw new HomesException(EnumError.INTERNAL_SERVER_ERROR.getSttusCd()) ;
        } 
		return fileidx ;
	}
	
	@Override
	public List<TodoVo> getTodoList( TodoVo paramVo ) {
		return mapper.selectTodoList(paramVo) ; 
	}
	
	@Override
	public CommonMap saveTodoList( Long mngrno,   List<TodoVo> paramVo ) {
		int in_co = 0 ; 
		for ( TodoVo todoVo : paramVo) {
			todoVo.setMngrno(0);
			todoVo.setUserno(0);
			String wkid = todoVo.getWkid() ; 
			int wk_co  = mapper.getWkidCount(todoVo) + 1 ;
			String idx = StringUtil.strLpad(String.valueOf(wk_co), 3, '0') ;
			todoVo.setWkid(wkid + idx);
			in_co += mapper.insertTodoList(todoVo) ; 
		}
		
		CommonMap insMap = new CommonMap() ; 
		insMap.put("inco"   , in_co) ; 
		insMap.put("message", in_co + "건이 등록되었습니다.") ;
		return insMap ;
	}
	
	@Override
	public CommonMap updateTodoList( Long mngrno,  TodoVo paramVo ) {
		int up_co = 0 ; 
		CommonMap insMap = new CommonMap() ; 
		paramVo.setMngrno(0);
		up_co = mapper.updateTodoList(paramVo) ; 
		insMap.put("upco"   , up_co) ; 
		insMap.put("message", up_co + "건이 수정되었습니다.") ;
		return insMap ;
	}

}
