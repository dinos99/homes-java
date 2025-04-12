package homes.comm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyUtil {

    private static final Logger Log = LogManager.getLogger(PropertyUtil.class); 
	private static Properties prop ; 
    
	public static Properties getProperty(String path, String propName) {
		FileReader resources = null ;
		prop = new Properties() ;
		try {
			resources= new FileReader(path + File.separator + propName );
			prop.load(resources);
		} catch (FileNotFoundException e) {
			Log.error("FileNotFoundException: {}파일을 찾을 수 없습니다.", propName);
		} catch (IOException e) {
			Log.error("IOException: {}파일을 로드하는데 실패하였습니다." );
		}
        return prop ; 
	}
	
	public static String getString(String key) {
		return prop.getProperty(key) ; 
	}
	
	public static String getStringVal(String key) {
		String propVal =prop.getProperty(key) ;  
		Log.info("*** propVal {}: {}", key, propVal) ; 
		return propVal == null ? "" : propVal ; 
	}
	
	public static long getLongVal(String key) {
		String propVal = prop.getProperty(key) ; 
		Log.info("*** propVal {}: {}", key, propVal) ; 
		return propVal == null ? 0l : Long.parseLong(propVal) ;
	}
	
}
