package homes.batch.job;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import homes.batch.mapper.BatchMapper;
import homes.comm.constants.EnumBatchJob;
import homes.comm.util.StringUtil;
import homes.comm.vo.CommonMap;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BJT001Job implements Job {	
	public Logger Log = LogManager.getLogger(BJT001Job.class) ;

	private final BatchMapper mapper ; 

	public final String BJT001    = EnumBatchJob.BJT001.getCode() ;
	public final String BTS_PROC  = EnumBatchJob.BTS001.getCode() ; 
	public final String BTS_DONE  = EnumBatchJob.BTS002.getCode() ; 
	
	public String bt_uuid = "" ; 
	
	@Transactional
	public int insertComplex(CommonMap cpmap) {
		return mapper.insertComplex(cpmap) ;
	}
	
	@Transactional 
	public int deleteComplex(CommonMap cpmap) {
		return mapper.deleteComplex(cpmap) ;
	}
	
	@Transactional
	public void start() {
		CommonMap btmap = new CommonMap() ; 
		this.bt_uuid  = UUID.randomUUID().toString() ;
		
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("exco"   , 0) ;
		btmap.put("batchty", BJT001) ;
		btmap.put("sttuscd", BTS_PROC ) ;
		btmap.put("message", "작업을 시작하였습니다.") ; 
		mapper.insertBatchjob(btmap) ; 
	}
	
	@Transactional
	public void finished(int ins_co) {
		CommonMap btmap = new CommonMap() ; 
		btmap.put("uuid"   , bt_uuid) ;
		btmap.put("exco"   , ins_co) ;
		btmap.put("batchty", BJT001) ;
		btmap.put("sttuscd", BTS_DONE ) ;
		btmap.put("message",  "단지정보를 등록하였습니다.(" + StringUtil.getCurrencyFormat(ins_co) + ")") ; 
		mapper.updateBatchjob(btmap) ; 
	}
	
	public int doExecute() {
		start() ;  
		int ins_co = 0 ; 
		int del_co = 0 ; 
		CommonMap cpmap = new CommonMap() ; 
		cpmap.put("nwOdGbCd"   , 9) ; 
		cpmap.put("creatUserNo", 0) ; 
		cpmap.put("updtUserNo" , 0) ; 
		List<CommonMap> cpxList = mapper.selectComplexList(cpmap) ;
		if (cpxList != null && cpxList.size() > 0) {
			for ( CommonMap cpxmap : cpxList ) {
				del_co += deleteComplex(cpxmap) ;
				CommonMap gmap = mapper.selectComplexCpxgno(cpxmap) ;
				String cpxgno = StringUtil.getStringValue((String)gmap.get("cpxgno"), "0001") ;
				cpxmap.put("cpxgno",cpxgno) ; 
				ins_co += insertComplex(cpxmap) ;
				Log.info("cpxno: {}, cpxno: {}, insert: {}", cpxmap.get("cpxno"), cpxgno, ins_co) ;
			}
		}
		Log.info("**** Complexinfo was deleted......[{}]", StringUtil.getCurrencyFormat(del_co)) ;
		Log.info("**** Complexinfo was inserted.....[{}]", StringUtil.getCurrencyFormat(ins_co)) ;
		finished(ins_co) ; 
		return ins_co ;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		doExecute() ;
	}

}
