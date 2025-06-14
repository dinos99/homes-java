package homes.system.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.vo.CommResponseVo;
import homes.comm.vo.CommonMap;
import homes.system.mapper.SystemMapper;
import homes.system.vo.BuldRegstrReqVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuldRegstrServiceImpl implements BuldRegstrService {
	public final Logger Log = LogManager.getLogger(BuldRegstrServiceImpl.class) ;

	private final SystemMapper mapper ;
	
	@Override
	@Transactional( readOnly = true )
	public CommResponseVo selectTotalLedgr(BuldRegstrReqVo paramVo) throws SQLException {
		Log.info("*** pageno: {}", paramVo.getPgno()) ;
		paramVo.setPage(); 
		Long t_cnt = mapper.selectTotalLedgrCount(paramVo) ;
		List<CommonMap> dataList = mapper.selectTotalLedgr(paramVo) ;
		return new CommResponseVo(t_cnt, paramVo.getPgno(), null, dataList) ;
	}

}
