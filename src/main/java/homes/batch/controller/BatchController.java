package homes.batch.controller;

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
import org.springframework.web.bind.annotation.RestController;

import homes.batch.service.BatchService;
import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumError;
import homes.comm.util.DateTimeUtil;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.exception.HomesException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
public class BatchController {
	public Logger Log = LogManager.getLogger(BatchController.class) ;  
	
	public final BatchService service ;  

	@PostMapping("/api/v1/batch/execute")
	public ResponseEntity<String> doExecuteBatch(HttpServletRequest request, @RequestBody BatchVo paramVo ) {
		String token = Optional.of(String.valueOf(request.getAttribute("accessToken"))).orElse("") ;  
		Log.info("jobid: {}, batchYn: {}", paramVo.getJobid(), paramVo.getBatchYn()) ;
		Log.info("accessToken: {}", token) ;
		BatchVo batVo = service.doExecute(paramVo, token) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(batVo)) ;
	}

	@GetMapping("/api/v1/batch/web-execute/{jobid}/{arcd}/{legcd}")
	public ResponseEntity<String> doExecuteBatch(@PathVariable String jobid, @PathVariable String arcd, @PathVariable String legcd ) {
		Log.info("jobid: {}, batchYn: {}", jobid, "N") ;
		CommonMap params = new CommonMap() ; 
		params.put("jobid"  , jobid) ; 
		params.put("arcd"   , arcd) ; 
		params.put("legcd"  , legcd) ;
		params.put("batchYn", "N") ;
		service.doExecute(jobid, params) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(params)) ;
	}
	@PostMapping("/api/v1/batch/btjobList")
	public ResponseEntity<String> btjobList( @RequestBody BatchReqVo paramVo ) {
//		Log.info("accessToken: {}", request.getAttribute("accessToken")) ;
		CommResponseVo resVo = null ;
//		String token = Optional.of(String.valueOf(request.getAttribute("accessToken"))).orElse("") ;  
		try {
			resVo = service.selectBatchJobList(paramVo) ;
		} catch ( SQLException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ; 
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(EnumError.INTERNAL_SERVER_ERROR.getSttusCd())) ;
		} catch ( HomesException e ) {
			Log.error("*** ApiError: () ", e.getMessage()) ;
	        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(e.getCode())) ;
		}
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(resVo)) ; 
	}

	@GetMapping("/api/v1/batch/web-execute/{jobid}/{batchde}")
	public ResponseEntity<String> doExecuteHBTJob(@PathVariable String jobid, @PathVariable String batchde ) {
		Log.info("*********************************************************************************") ; 
		Log.info("*** jobid: [{}], batchde: {}", jobid, batchde) ;
		Log.info("*** [started at: {}]", DateTimeUtil.getCurrentDateTime()) ;
		CommonMap params = new CommonMap() ;
		params.put("jobid"  , jobid) ; 
		params.put("batchde", batchde) ; 
		BatchVo batchvo = service.doExecute(jobid, params) ;
		Log.info("*** [finished at: {}]", DateTimeUtil.getCurrentDateTime()) ;  
		Log.info("*********************************************************************************") ;
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(batchvo)) ;
	}
}
