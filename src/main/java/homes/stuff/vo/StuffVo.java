package homes.stuff.vo;

import java.util.List;

import homes.comm.vo.CommonMap;
import lombok.Data;

@Data
public class StuffVo {
	int flno ;
	int floorco ; 
	int roomco ; 
	int owseq ; 
	int histno ; 
	int hasRelcount ; 
	
	int officeno ; 
	int brkno ; 
	int userno ; 
	int custno ;  
	
	
	String stuffno ; /* key */ 
	
	String htbdno ; /* 홈즈_건물관리대장_마스터_PK */ 
	String hbdno  ; /* 홈즈_건물관리대장_표제부_PK */
	String hpsno  ; /* 홈즈_건물관리대정_전유부_PK */ 
	String flgbcd ; /* 건물구분 */
	
	String hppscd ; 
	
	String cplxno ;
	String pssionno ;
	String owno ; 
	String sfsttus ; 
	String arcd ; 
	String arcode ; 
	String legcd ;  
	String bun ; 
	String ji ;
	String buldnm ;
	String estcd ;
	String ppscd ;
	String etcppscd ; 
	String estTynm ;
	String x ;
	String y ;

	String kakaox;  
	String kakaoy;
	String naverx; 
	String navery; 
	
	String stffnm01 ; 
	String stffnm02 ;
	String ownerTy ; 
	String ownernm ; 
	String ccbCd ;
	String cttpc ; 
	String owsttus ;
	String cntcpersnYn ;
	
	/* 도로명 조회 */ 
	String rdmainb ; /* 도로명_본_번 */
	String rdsubb ;  /* 도로명_부_번 */ 
	String rdcode ;  /* 도로명_코드 */ 
	String rdlegcd ; /* 도로명_법정동코드 */ 
	List<CommonMap> remap ; /* 관련지번 목록 */  
	
	/* 홈즈_건축물관리대장 PK */
	String useYn ;  /* 사용_여부 */
	String buldgb ; /* 건물_구분 ([1]: 일반건물, [2]: 집합건물) */  
}
