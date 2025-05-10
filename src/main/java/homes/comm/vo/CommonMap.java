package homes.comm.vo;

import java.math.BigDecimal;
import java.util.HashMap;

import homes.comm.util.StringUtil;

public class CommonMap extends HashMap<Object, Object> {
	static final long serialVersionUID = 1L;

	public Object put(Object key, Object val) {
		return super.put(StringUtil.convCamelCase(String.valueOf(key)), val) ; 
	}
	
	public Long getLongValue(Object key) {
		Object val = super.get(key) ;
		if ( val instanceof java.lang.Long ) {
			return ( Long ) val ; 
		} else if ( val instanceof java.math.BigDecimal ) {
			BigDecimal bd = new BigDecimal((long)val) ; 
			return bd.longValue() ; 
		}
		return 0l ; 
	}
}
