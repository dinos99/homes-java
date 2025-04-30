package homes.popup.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import homes.comm.vo.CommonMap;
import homes.popup.mapper.PopupMapper;
import homes.popup.vo.PopCommReqVo;
import homes.popup.vo.PopCommResVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopupServiceImpl implements PopupService {

	public Logger Log = LogManager.getLogger(PopupServiceImpl.class) ; 
	
	private final PopupMapper mapper ; 

	
	@Override
	public int selectChernmCount(PopCommReqVo paramVo) throws SQLException {
		return mapper.selectChernmCount(paramVo);		
	}
	@Override
	public List<CommonMap> selectChernmList(PopCommReqVo paramVo) throws SQLException {		
		return mapper.selectChernmList(paramVo);
	}
	
	@Override
	public PopCommResVo selectChernm(PopCommReqVo paramVo) throws SQLException {
		PopCommReqVo reqVo = new PopCommReqVo() ;
		int pageno    = paramVo.getPageno();
		int offset    = paramVo.getOffset() ; 
		String usernm = paramVo.getUsernm();
		 
		int stno = ( pageno - 1 ) * offset ; 
		int edno = stno + offset ; 
		
		reqVo.setPageno(pageno);
		reqVo.setStno(stno);
		reqVo.setEdno(edno); ; 
		reqVo.setOffset(offset) ; 
		reqVo.setUsernm(usernm);
		Log.info("*** paramVo: {}", reqVo) ;
		
		int totalCount = selectChernmCount(reqVo) ;
		int totalpg = (int) Math.ceil((totalCount * 1.0) / offset) ;
		
		CommonMap page = new CommonMap() ;
		page.put("totalcnt"  , totalCount) ; 
		page.put("totalpg"   , totalpg) ; 
		page.put("pageno"    , pageno) ; 
		page.put("stno"      , stno) ;  
		page.put("edno"      , edno) ;
		page.put("offset"    , offset) ; 
		List<CommonMap> dataList = selectChernmList(reqVo) ;
		
		PopCommResVo resVo = new PopCommResVo() ;
		resVo.setPage(page) ; 
		resVo.setDataList(dataList) ; 
		return resVo ; 
	}

}
