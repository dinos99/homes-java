package homes.comm.util;

import org.springframework.util.ObjectUtils;

public class ObjectUtil {
	
	public static boolean isEmpty(Object o) {
		return ObjectUtils.isEmpty(o) ; 
	}
	
	public static boolean isNotEmpty(Object o) {
		return !ObjectUtils.isEmpty(o) ; 
	}
	
	public static boolean isEmpty(Object[] o) {
		return ObjectUtils.isEmpty(o) ; 
	}
	
	public static boolean isNotEmpty(Object[] o) {
		return !ObjectUtils.isEmpty(o) ;
	}
	
}
