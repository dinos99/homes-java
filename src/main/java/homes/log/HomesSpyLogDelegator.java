package homes.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.log4jdbc.Properties;
import net.sf.log4jdbc.log.SpyLogDelegator;
import net.sf.log4jdbc.sql.Spy;
import net.sf.log4jdbc.sql.resultsetcollector.ResultSetCollector;
import net.sf.log4jdbc.sql.resultsetcollector.ResultSetCollectorPrinter;

public class HomesSpyLogDelegator implements SpyLogDelegator {

    private static final Logger sqlOnlyLogger   = LogManager.getLogger("homes.log.sqlonly");
    private static final Logger sqlTimingLogger = LogManager.getLogger("homes.log.sqltiming");
    private static final Logger resultSetLogger = LogManager.getLogger("homes.log.resultset");


    private String getQueryOperation(String sql)  {
        if (sql == null) return "";
        sql = sql.trim();
        
        if (sql.length() < 6) return ""; 
        return sql.substring(0, 6).toUpperCase();
    }
    
    private boolean shouldSqlBeLogged(String operation) {
        return 
                (operation == null) ||
                (Properties.isDumpSqlSelect() && "select".equals(operation)) ||
                (Properties.isDumpSqlInsert() && "insert".equals(operation)) ||
                (Properties.isDumpSqlUpdate() && "update".equals(operation)) ||
                (Properties.isDumpSqlDelete() && "delete".equals(operation)) ||
                (Properties.isDumpSqlCreate() && "create".equals(operation));
    }
    
	@Override
	public boolean isJdbcLoggingEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime) {
        String operation = getQueryOperation(sql) ;
        sqlTimingLogger.info("[ SQL {} 👀🎆🎆🎆\rn{}", operation, execTime, sql);
	}

	@Override
	public void methodReturned(Spy spy, String methodCall, String returnMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void constructorReturned(Spy spy, String constructionInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sqlOccurred(Spy spy, String methodCall, String sql) {
        String operation = getQueryOperation(sql) ;
		sqlOnlyLogger.info("[ SQL - {} ]  \r\n{}", operation, sql) ;
	}

	@Override
	public void sqlTimingOccurred(Spy spy, long execTime, String methodCall, String sql) {
        String operation = getQueryOperation(sql) ;
        if (Properties.isDumpSqlFilteringOn() && !this.shouldSqlBeLogged(operation)) {
        	sqlTimingLogger.info("*** isDumpSqlFilteringOn && !shouldSqlBeLogged({})", operation);
            return;
        }
        sqlTimingLogger.info("[ SQL - {} ][⏱️ {} ms ]\r\n{}", operation, execTime, sql);
	}

	@Override
	public void connectionOpened(Spy spy, long execTime) {}

	@Override
	public void connectionClosed(Spy spy, long execTime) {}

	@Override
	public void connectionAborted(Spy spy, long execTime) {}

	@Override
	public void debug(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isResultSetCollectionEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isResultSetCollectionEnabledWithUnreadValueFillIn() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void resultSetCollected(ResultSetCollector resultSetCollector) {
        String resultToPrint = new ResultSetCollectorPrinter().getResultSetToPrint(resultSetCollector);		
		resultSetLogger.info(resultToPrint);
	}
}
