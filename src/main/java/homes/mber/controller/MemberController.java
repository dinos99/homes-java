package homes.mber.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.service.CommonService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommUserReqVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mber")
public class MemberController {
	public Logger Log = LogManager.getLogger(MemberController.class) ;  

	private final CommonService service ;
	
	@PostMapping("/add-member")
	public ResponseEntity<String> insertMember(@RequestBody CommUserReqVo paramVo) {
		Log.info("*** request: /api/v1/mber/add-member") ;
		try {
			
			service.insertCommuser(paramVo) ;
			long userno = service.selectLastid() ;
			
			paramVo.setUserno(userno) ; 
			
		} catch ( SQLException e ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( RuntimeException e) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(paramVo)) ;
	}
	
	
	
	
}
