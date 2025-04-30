package homes.comm.util;

public class TestUtil {
	
	
	public static void main( String[] args ) {

		String txt = "arCodeList1";
		boolean result1 = txt.matches("[A-Z0-9]+"); // 숫자로 이루어져 있는지
		System.out.println(result1);
	}
	
}
