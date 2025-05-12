package homes.manager.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.exception.HomesException;
import homes.manager.service.ManagerService;
import homes.manager.vo.ManagerVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerController {
	public Logger Log = LogManager.getLogger(ManagerController.class) ;
	
	private final ManagerService service ; 

	@PostMapping("/api/v1/manager/sign-up")
	public ResponseEntity<String> Registbroker(@RequestBody ManagerVo paramVo ) {
		ManagerVo mVo = null ; 
		try {
			mVo = service.registmanager( paramVo ) ;
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(mVo)) ;
	}
	
	
}
