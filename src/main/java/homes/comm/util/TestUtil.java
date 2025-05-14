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
		  System.out.println(StringUtil.convCamelCase("SUB_BULD_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("PLOT_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("BULD_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("BULD_LND_RT")) ; 
		  System.out.println(StringUtil.convCamelCase("TOTAL_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("BULK_CALC_TOT_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("BULK_RT")) ; 
		  System.out.println(StringUtil.convCamelCase("IN_MECHA_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("OUT_MECHA_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("IN_INDPNT_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("OUT_INDPNT_AR")) ; 
		  System.out.println(StringUtil.convCamelCase("ENERGY_REDCN_RT")) ; 
	}
	
}
