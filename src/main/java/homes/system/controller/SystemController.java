package homes.system.controller;

import java.sql.SQLException;
import java.util.List;

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
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import homes.system.service.BuldRegstrService;
import homes.system.service.SystemService;
import homes.system.vo.BuldRegstrReqVo;
import homes.system.vo.DomainListVo;
import homes.system.vo.DomainVo;
import homes.system.vo.SystemReqVo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SystemController {
	public Logger Log = LogManager.getLogger(SystemController.class) ;
	
	private final SystemService service ;
	private final BuldRegstrService bdservice ;
	
	@PostMapping("/api/v1/system/domainList")
	public ResponseEntity<String> getDomainList(@RequestBody SystemReqVo reqVo ) {
		CommResponseVo resVo = null ; 
		try {
			resVo = service.domainList(reqVo) ; 
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}

	@PostMapping("/api/v1/system/exdomainList")
	public ResponseEntity<String> getExdomainList(@RequestBody DomainListVo dmListVo ) {
		List<CommonMap> domainList = null ;
		try {
			Log.info("*** DomainList: {}", dmListVo ) ; 
			domainList = service.selectExistsDomainList(dmListVo) ;
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(domainList)) ;
	}
	
	@PostMapping("/api/v1/system/modifydomain")
	public ResponseEntity<String> Modifydomain(@RequestBody DomainVo paramVo ) {
		try {
			Log.info("*** paramVo: {}", paramVo ) ; 
			int co = service.modifyDomain(paramVo) ;
			if ( "Y".equals(paramVo.getIsmodify())) {
				paramVo.setModco(co) ;
			} else {
				paramVo.setInsco(co) ; 
			}
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(paramVo)) ;
	}

	@PostMapping("/api/v1/system/totalLedger")
	public ResponseEntity<String> totalLedger(@RequestBody BuldRegstrReqVo paramVo ) {
		CommResponseVo resVo = null ; 
		try {
			resVo = bdservice.selectTotalLedgr(paramVo) ;
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
