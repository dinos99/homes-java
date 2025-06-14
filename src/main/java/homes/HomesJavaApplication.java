package homes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomesJavaApplication {

	public static void main(String[] args) {
		/* **********************************************************************************
		 * 아래 호출보다 Log4jSpyLogDelegator 호출이 더 빠름
		 * 찾아보니 log4jdbc.log4j2.properties이더라 
		 * **********************************************************************************/
//	    System.setProperty("log4jdbc.spylogdelegator.name", "homes.log.HomesSpyLogDelegator");
//		System.out.println(">> SpyLogDelegator class: " + net.sf.log4jdbc.log.SpyLogFactory.getSpyLogDelegator().getClass());
		SpringApplication.run(HomesJavaApplication.class, args);
	}

}
