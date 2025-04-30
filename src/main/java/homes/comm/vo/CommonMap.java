package homes.comm.vo;

import java.util.HashMap;

import homes.comm.util.StringUtil;

public class CommonMap extends HashMap<Object, Object> {
	static final long serialVersionUID = 1L;

	public Object put(Object key, Object val) {
		return super.put(StringUtil.convCamelCase(String.valueOf(key)), val) ; 
	}
	
	
}
