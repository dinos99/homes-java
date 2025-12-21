package homes.comm.vo;

import java.util.Map;

public class MapUtil {
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }
    
    public static boolean isNotEmpty(final Map<?,?> map) {
    	return !MapUtil.isEmpty(map);
    }
    
    public static boolean isEmpty(final Map<?,?> map, String key) {
    	if (MapUtil.isNotEmpty(map)) {
    		if ( map.get(key) == null ) {
    			return true ; 
    		} else {
    			Object val = map.get(key) ;
    			if ( val instanceof String ) {
    				return (String)val == null || "".equals((String)val); 
    			} else {
    				return val == null ;
    			}
    		}
    	}
    	return false ; 
    }
    
    public static boolean isNotEmpty(final Map<?,?> map, String key) {
    	return !MapUtil.isEmpty(map, key) ; 
    }
}
