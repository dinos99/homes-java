package homes.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.api.kakao.service.KakaoApiService;
import homes.api.kakao.vo.KakaoApiReqVo;
import homes.api.naver.service.NaverApiService;
import homes.api.naver.vo.NaverClientVo;
import homes.comm.util.JsonUtil;
import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoApiController {
	public Logger Log = LogManager.getLogger(KakaoApiController.class) ;  
	
	public final NaverApiService naverService ;  
	public final KakaoApiService kakaoService ;  

	@PostMapping(value = "/naver/addr", produces = "application/json; charset=UTF-8")
	public ResponseEntity<String> LocalAddrSearch(HttpServletRequest request, @RequestBody NaverClientVo paramVo ) {
		try {
			String addr = naverService.getLocalAddress(paramVo) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(addr)) ;
		} catch ( HomesException he ) {
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(400)) ;
		}
	}
	@PostMapping(value = "/kakao/transcoord", produces = "application/json; charset=UTF-8")
	public ResponseEntity<String> KakaoTranscoord(HttpServletRequest request, @RequestBody KakaoApiReqVo paramVo ) {
		try {
			String addr = kakaoService.getKakaoTranscoord(paramVo) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(addr)) ;
		} catch ( HomesException he ) {
			he.printStackTrace() ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(400)) ;
		}
	}
}
