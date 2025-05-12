package homes.project.controller;

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
import homes.comm.vo.CommResponseVo;
import homes.exception.HomesException;
import homes.project.service.ProjectService;
import homes.project.vo.ProjectReqVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {
	public Logger Log = LogManager.getLogger(ProjectController.class) ;

	private final ProjectService service ; 

	
	@PostMapping("/api/v1/project/homesuserList")
	public ResponseEntity<String> Registbroker(@RequestBody ProjectReqVo paramVo ) {
		CommResponseVo resVo = null ; 
		try {
			resVo = service.homesuserList(paramVo) ; 
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}
	

	
}
