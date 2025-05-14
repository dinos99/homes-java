package homes.batch.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import homes.batch.service.BatchService;
import homes.comm.util.JsonUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BatchController {
	public Logger Log = LogManager.getLogger(this.getClass()) ; 
	
	private final BatchService service ; 

	@PostMapping("/api/v1/batch/{jobid}")
	public ResponseEntity<String> executeBatchJob( @PathVariable("jobid") String jobic ) {
		CommonMap cmap = new CommonMap() ;
		service.doExecute(jobic) ; 
        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.getJson(cmap)) ;
	}
	
	
	
}
