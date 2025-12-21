package homes.api.buld.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import homes.api.buld.service.BuldApiServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BuldApiController {
	public Logger Log = LogManager.getLogger(BuldApiController.class) ;
	public final BuldApiServiceImpl apiService ;

}
