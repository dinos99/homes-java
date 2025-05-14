package homes.comm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyUtil {

    private static final Logger Log = LogManager.getLogger(PropertyUtil.class);
	private static final String PROFILE = System.getProperty("profile") ; 
	private static final String PROP_BASE_PATH = System.getProperty("java.class.path").split(File.pathSeparator)[0] ; 
	private static final String HOMES_PROP_NAME = "homes" ;
	private static Properties prop ; 


	public static Properties getProperty() {
		FileReader resources = null ;
		prop = new Properties() ;
		try {
			resources= new FileReader(PROP_BASE_PATH + File.separator + HOMES_PROP_NAME+ "-" + PROFILE + ".properties");
			prop.load(resources);
		} catch (FileNotFoundException e) {
			Log.error("FileNotFoundException: {}파일을 찾을 수 없습니다.", HOMES_PROP_NAME);
		} catch (IOException e) {
			Log.error("IOException: {}파일을 로드하는데 실패하였습니다." );
		}
        return prop ; 
	}
	
	public static Properties getProperty( String propName ) {
		FileReader resources = null ;
		prop = new Properties() ;
		try {
			Log.info("*** PROP_BASE_PATH: {}", PROP_BASE_PATH + File.separator + propName + "-" + PROFILE + ".properties");
			resources= new FileReader(PROP_BASE_PATH + File.separator + propName + "-" + PROFILE + ".properties");
			prop.load(resources);
		} catch (FileNotFoundException e) {
			Log.error("FileNotFoundException: {}파일을 찾을 수 없습니다.", propName);
		} catch (IOException e) {
			Log.error("IOException: {}파일을 로드하는데 실패하였습니다." );
		}
        return prop ; 
	}
	

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
	
	public static String getPropVal(String key) {
		getProperty() ;
		return getString(key) ;
	}
	
	public static String getString(String key) {
		String propVal =prop.getProperty(key) ;  
		Log.info("*** propVal {}: {}", key, propVal) ; 
		return Optional.ofNullable(propVal).orElse("") ; 
	}
	
	public static String getStringVal(String key) {
		String propVal =prop.getProperty(key) ;  
		Log.info("*** propVal {}: {}", key, propVal) ; 
		return Optional.ofNullable(propVal).orElse("") ; 
	}
	
	public static long getLongVal(String key) {
		String propVal = prop.getProperty(key) ; 
		Log.info("*** propVal {}: {}", key, propVal) ; 
		return propVal == null ? 0l : Long.parseLong(propVal) ;
	}
	
}
