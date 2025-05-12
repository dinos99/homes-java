package homes.comm.vo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommResponseVo {

	public CommResponseVo( Long tcnt, int pgno, CommonMap dMap, List<CommonMap> dList ) {
		this.t_cnt = tcnt ;
		this.pgno  = pgno ;
		
		/* 기본값 셋팅 */ 
		this.order = "DESC" ;
		this.r_cnt = 10 ; 
		this.f_pageno = 1 ;
		
		if ( this.t_cnt == 0 ) {
			this.l_pageno = 1 ; 
		} else {
			Long lastpg = Math.ceilDiv(t_cnt, r_cnt) ;
			this.l_pageno = Integer.parseInt(String.valueOf(lastpg)) ;
		}
		
		if ( this.pgno > this.l_pageno ) {
			this.pgno = this.l_pageno ; 
		}
		
		/* 페이지번호를 10개씩 보여준다(고정) ex) 1page ~ 10page */ 
		this.pg_st = ( this.pgno / 10 ) + 1 ; 
		this.pg_ed = this.pg_st + 9 ; 
		
		if ( this.pg_ed > this.l_pageno ) this.pg_ed = this.l_pageno ; 
		
		this.dataMap = dMap ; 
		this.dataList = dList ;
	}
	
	private Long t_cnt ; /* 전체카운트 */ 
	private Long r_num ; /* rownum */ 
	
	private int pg_cnt ;
	private int pg_st ;
	private int pg_ed ; 
	
	private int r_cnt ; /* rowCount ( 한 화면에 보여줄 행의개수 ) */
	private int pgno ;  /* 현재 페이지번호 */
	private int stno ;  /* 시작위치 */
	private int edno ;  /* 종료위치 */
	
	private int f_pageno ; 
	private int l_pageno ; 
	
	private int ins_cnt ; /* insert count */
	private int upt_cnt ; /* update count */
	private int del_cnt ; /* delete count */ 
	
	private String order ; 
	
	private CommonMap dataMap ;
	private List<CommonMap> dataList ; 
	
}
