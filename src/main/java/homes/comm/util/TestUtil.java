package homes.comm.util;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
	
	
	public static void main( String[] args ) {
/*
		String txt = "arCodeList1";
		boolean result1 = txt.matches("[A-Z0-9]+"); // 숫자로 이루어져 있는지
		System.out.println(System.currentTimeMillis()) ; 
		System.out.println(result1);
*/
    	List<String> upcdList = new ArrayList<String>() ;
    	upcdList.add("EST000") ; 
    	System.out.println(upcdList.contains("EST000")) ; 
	}
	
}
