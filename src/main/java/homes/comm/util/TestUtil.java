package homes.comm.util;

public class TestUtil {
	
	
	public static void main( String[] args ) {
/*
		String txt = "arCodeList1";
		boolean result1 = txt.matches("[A-Z0-9]+"); // 숫자로 이루어져 있는지
		System.out.println(System.currentTimeMillis()) ; 
		System.out.println(result1);
    	List<String> upcdList = new ArrayList<String>() ;
    	upcdList.add("EST000") ; 
    	System.out.println(upcdList.contains("EST000")) ; 
		Long t_cnt = 110l ;
		int  r_cnt = 3 ; 
		System.out.println(Math.ceilDiv(t_cnt, r_cnt)) ;		
		System.out.println("*** execute batchjob, jobid[BTJ001] started at " + DateTimeUtil.convertTimeStampToString(System.currentTimeMillis(), "yyyy.MM.dd HH:mm:ss.SSS")) ;
 */  
		String[] headers = "1000000000000000052605||1|일반|1|총괄표제부| |||||||||||2||||||UQB200|||생산관리지역|||20220818".split("[|]") ;
		for ( int i = 0; i < headers.length; i ++ ) {
			System.out.println("headers[" + i + "]:" + headers[i]) ;
		}
	}
	
}
