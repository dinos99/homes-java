package homes.buldapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.buldapi.service.BuldHubService;
import homes.buldapi.vo.BuldHubReqVo;
import homes.buldapi.vo.BuldHubResVo;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.JsonUtil;
import homes.comm.util.RequestUtil;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
public class BuldHubController {
	public Logger Log = LogManager.getLogger(BuldHubController.class) ;  
	
	public final BuldHubService service ;

	@PostMapping("/api/v1/buldapi/bapi/0101")
	public ResponseEntity<String> Bapi0101(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장 관련지번 조회 ") ;  
		Log.info("*********************************************************************************") ;
		CommResponseVo outVo = service.getRdRelateJibun(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(outVo)) ;
	}
	
	@PostMapping("/api/v1/buldapi/bapi/0201")
	public ResponseEntity<String> Bapi0201(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장  기본개요 조회 ") ;  
		Log.info("*********************************************************************************") ;
		CommResponseVo outVo = service.getBaseOutLine(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(outVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0202")
	public ResponseEntity<String> Bapi0202(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장  기본개요 등록 ") ;  
		Log.info("*********************************************************************************") ;

		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		BuldHubResVo resVo = service.insertHbdSummary(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}
	
	@PostMapping("/api/v1/buldapi/bapi/0301")
	public ResponseEntity<String> Bapi0301(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장 API 표제부 조회 ") ;  
		Log.info("*********************************************************************************") ;
		Log.info("*** paramVo: {}", paramVo) ;  
		CommResponseVo bdLedgrVo = service.getLedgrinfo(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(bdLedgrVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0302")
	public ResponseEntity<String> Bapi0302(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장 표제부 등록 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		BuldHubResVo resVo = service.insertHbdLedgr(paramVo) ;  
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0401")
	public ResponseEntity<String> Bapi0401(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장 API 총괄 조회 ") ;  
		Log.info("*********************************************************************************") ;
		CommResponseVo bdLedgrVo = service.getRecapTitleinfo(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(bdLedgrVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0402")
	public ResponseEntity<String> Bapi0402(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** 건축물관리대장 마스터 등록 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		BuldHubResVo resVo = service.insertHdbLedgrMst(paramVo) ;  
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}
	

	@PostMapping("/api/v1/buldapi/bapi/0501")
	public ResponseEntity<String> Bapi0501(@RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*********************************************************************************") ;
		List<CommonMap> buldList = service.selectBuldList(paramVo) ;  
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(buldList)) ;
	}
	@PostMapping("/api/v1/buldapi/bapi/0502")
	public ResponseEntity<String> Bapi0502(@RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0502] 건축물관리대장 마스터 건물(동) 조회 ") ;  
		Log.info("*********************************************************************************") ;
		List<CommonMap> buldList = service.selectDongList(paramVo) ;  
		return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(buldList)) ;
	}
	@PostMapping("/api/v1/buldapi/bapi/0503")
	public ResponseEntity<String> Bapi0503(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0503] 건축물관리대장 API 층별개요 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		CommResponseVo floorVo = service.getFloorOutLine(paramVo) ;  
		return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(floorVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0504")
	public ResponseEntity<String> Bapi0504(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0504] 건축물관리대장 층별개요(임시) 등록 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		BuldHubResVo resVo = service.insertTempLedgrFloor(paramVo) ;  
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0505")
	public ResponseEntity<String> Bapi0505(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0505] 건축물관리대장 층별개요 등록 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		paramVo.setBatchAt("4") ; 
		paramVo.setBatchde(DateTimeUtil.getToday()) ;
		BuldHubResVo resVo = service.insertLedgrFloor(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}

	@PostMapping("/api/v1/buldapi/bapi/0601")
	public ResponseEntity<String> Bapi0601(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0601] 건축물관리대장 API 전유부조회 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		CommResponseVo floorVo = service.getBrExposInfo(paramVo) ;  
		return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(floorVo)) ;
	}
	@PostMapping("/api/v1/buldapi/bapi/0701")
	public ResponseEntity<String> Bapi0701(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0701] 건축물관리대장 API 전유부조회 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		CommResponseVo floorVo = service.getBrExposPubuseAreaInfo(paramVo) ;  
		return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(floorVo)) ;
	}
	@PostMapping("/api/v1/buldapi/bapi/0702")
	public ResponseEntity<String> Bapi0702(HttpServletRequest request, @RequestBody BuldHubReqVo paramVo ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** [0702] 건축물관리대장 전유공용면적(전유부) 등록 ") ;  
		Log.info("*********************************************************************************") ;
		int mngrno = RequestUtil.getUserno(request) ;
		paramVo.setMngrno(mngrno) ;
		paramVo.setBatchAt("6") ; 
		paramVo.setBatchde(DateTimeUtil.getToday()) ;
		BuldHubResVo resVo = service.insertLedgrPssion(paramVo) ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ;
	}

	
	
}
