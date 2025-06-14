package homes.comm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomesProperty {
    private static final Logger Log = LogManager.getLogger(HomesProperty.class);
	private static final String PROFILE = System.getProperty("profile") ; 
	private static final String HOMES_PROP_PATH = System.getProperty("homes.property.path") + File.separator + "homes-" + PROFILE+ ".properties" ;
	private static Properties prop ;  
	
	public static Properties getProperty() {
		FileReader resources = null ;
		prop = new Properties() ;
		try {
			resources= new FileReader(HOMES_PROP_PATH);
			prop.load(resources);
		} catch (FileNotFoundException e) {
			Log.error("*** home property: {}", HOMES_PROP_PATH ) ;
			Log.error("FileNotFoundException: homes-{}.properties 파일을 찾을 수 없습니다.", PROFILE ) ;
		} catch (IOException e) {
			Log.error("IOException: homes-{}.properties 파일을 로드하는데 실패하였습니다.", PROFILE );
		}
        return prop ; 
	}
	
	public static String getStringVal( String key ) {
		getProperty() ; 
		return Optional.ofNullable(prop.getProperty(key)).orElse("") ; 
	}
	
	public static long getLongVal(String key) {
		String propVal = getPropVal( key ) ;
		return propVal == null ? 0l : Long.parseLong(propVal) ;
	}
	
	public static String getPropVal( String key ) {
		return getStringVal(key) ;
	}
}
