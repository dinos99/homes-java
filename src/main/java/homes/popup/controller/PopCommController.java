package homes.popup.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.popup.service.PopupService;
import homes.popup.vo.PopCommReqVo;
import homes.popup.vo.PopCommResVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PopCommController {
	
	public Logger Log = LogManager.getLogger(PopCommController.class) ; 
	
	private final PopupService service ; 
	
	@PostMapping( value = "/api/v1/popup/search/{target}") 
	public ResponseEntity<String> popSearch(@PathVariable String target, @RequestBody PopCommReqVo paramVo ) {
		Log.info("*** target: {}, param: {}", target, paramVo) ;
		PopCommResVo  resVo = null ; 
		try {
			if ( "chernm".equals(target)) {
				resVo = service.selectChernm(paramVo) ;
			}
		} catch ( SQLException e ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
		 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}
	
	
}
