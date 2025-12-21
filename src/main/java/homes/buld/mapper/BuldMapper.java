package homes.buld.mapper;

import org.apache.ibatis.annotations.Mapper;

import homes.comm.vo.CommonMap;
import homes.stuff.vo.StuffVo;

@Mapper
public interface BuldMapper {
	
	public int getTitleLedgrChngSeq( String buldno ) ;
	/* 용도변경 변경번호 조회 */ 
	public int getLedgrPpsHistno( String buldno ) ;
	
	/* 용도변경이력 등록(원본데이터) */ 
	public int insertLedgrPpscdHist( StuffVo paramVo ) ;
	/* 건물용도 변경 */ 
	public int updateLedgrPpscd( StuffVo paramVo ) ;
	
	/* 용도코드가 변경되었는지 조회한다. */ 
	public CommonMap isUpdatePpsCd( StuffVo paramVo ) ; 
	
}
