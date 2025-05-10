package homes.mber.controller;

import java.sql.SQLException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.comm.constants.EnumError;
import homes.comm.service.CommonService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommUserReqVo;
import homes.comm.vo.CommonMap;
import homes.mber.service.MemberService;
import io.jsonwebtoken.io.Decoders ;
import io.jsonwebtoken.io.Encoders ;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mber")
public class MemberController {
	public Logger Log = LogManager.getLogger(MemberController.class) ;  

	private final CommonService commService ;
	private final MemberService service ; 
	
	@PostMapping("/add-member")
	public ResponseEntity<String> insertMember(@RequestBody CommUserReqVo paramVo) {
		Log.info("*** request: /api/v1/mber/add-member") ;
		try {
			Long userno = commService.insertCommuser(paramVo) ;
			Log.info("*** userno: {}", userno) ; 
			String cttpc = Encoders.BASE64.encode(paramVo.getMbleCttpc().getBytes()) ; 
			paramVo.setUserno(userno) ; 
			paramVo.setPassword("") ; 
			paramVo.setMbleCttpc(cttpc) ;
		} catch ( SQLException e ) {
			Log.error("*** ApiError: {} ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( RuntimeException e) {
			Log.error("*** ApiError: {} ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(paramVo)) ;
	}
	
	@GetMapping("/isuser/{email}")
	public ResponseEntity<String> isEmailUser(@PathVariable String email ) {
		Log.info("*** request: /api/v1/mber/isuser/{}", email) ;
		CommonMap map = new CommonMap() ; 
		try {
			String existYn = Optional.ofNullable(service.isExistUserByEmail(email)).orElse("N") ;
			if ( "Y".equals(existYn)) {
		        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.IS_EXIST_EMAIL.getSttusCd())) ;
			} else {
				map.put("existYn", existYn) ;
		        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(map)) ; 
			}
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
	}

	@GetMapping("/ismbuser/{mblecttpc}")
	public ResponseEntity<String> isMobileUser(@PathVariable String mblecttpc ) {
		String cttpc = new String(Decoders.BASE64.decode(mblecttpc)) ;
		Log.info("*** request: /api/v1/mber/ismbuser/{}", cttpc) ;
		CommonMap map = new CommonMap() ; 
		try {
			String existYn = Optional.ofNullable(service.isExistUserByCttpc(cttpc)).orElse("N") ;
			if ( "Y".equals(existYn)) {
		        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.IS_EXIST_MOBILE.getSttusCd())) ;
			} else {
				map.put("existYn", existYn) ;
		        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(map)) ; 
			}
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		}
	}
	
}
