package homes.batch.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import homes.batch.service.BatchService;
import homes.batch.vo.BatchReqVo;
import homes.batch.vo.BatchVo;
import homes.comm.constants.EnumError;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommResponseVo;
import homes.exception.HomesException;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
public class BatchController {
	public Logger Log = LogManager.getLogger(BatchController.class) ;  
	
	public final BatchService service ;  

	@PostMapping("/api/v1/batch/execute")
	public ResponseEntity<String> doExecuteBatch(@RequestBody BatchVo paramVo ) {
		Log.error("jobid: {}, batchYn: {}", paramVo.getJobid(), paramVo.getBatchYn()) ;
		BatchVo batVo = service.doExecute(paramVo) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(batVo)) ;
	}

	@PostMapping("/api/v1/batch/btjobList")
	public ResponseEntity<String> btjobList(@RequestBody BatchReqVo paramVo ) {
		CommResponseVo resVo = null ; 
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
	
	

}
