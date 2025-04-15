package homes.comm.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateTimeUtil {
	
	/* Date to LocalDate */
	public static LocalDate ToLocalDate(java.sql.Date date) {
	    return date == null ? null : date.toLocalDate();
	}
	
	/* Time to LocalTime */
	public static LocalTime ToLocalTime(java.sql.Time time) {
	    return time == null ? null : time.toLocalTime();
	}
	
	/* TimeStamp to String */
	public static String convertTimeStampToString(Timestamp timestamp, String format) {
	    if (timestamp == null) return "";

	    try {
	        Date date = new Date();
	        date.setTime(timestamp.getTime());
	        return new SimpleDateFormat(format).format(date);
	    } catch (RuntimeException e) {
	        return "";
	    }
	}
	
	/* LocalDateTime to String */
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime, String format) {
	    if (localDateTime == null) return "";

	    try {
	        return localDateTime.format(DateTimeFormatter.ofPattern(format));
	    } catch (RuntimeException e) {
	        return "";
	    }
	}
	
	/* LocalTime to String */ 
	public static String convertLocalTimeToString(LocalTime localTime, String format) {
	    if (localTime == null) return "";

	    try {
	        return localTime.format(DateTimeFormatter.ofPattern(format));
	    } catch (RuntimeException e) {
	        return "";
	    }
	}
	
	/* String to LocalDateTime */ 
	public static LocalDateTime convertStringToLocalDateTime(String dateTime, String format) {
	    if (dateTime == null) return null;

	    try {
	        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format));
	    } catch (RuntimeException e) {
	        return null;
	    }
	}

	/* String to Timestamp */
	public static Timestamp convertStringToTimestamp(String dateTime, String format) {
	    if (dateTime == null) return null;

	    try {
	        Date date = new SimpleDateFormat(format).parse(dateTime);
	        return new Timestamp(date.getTime());
	    } catch (ParseException e) {
	        return null;
	    }
	}
	
	/* String to LocalDate */
	public static LocalDate convertStringToLocalDate(String dateTime, String format) {
	    if (dateTime == null) return null;

	    try {
	        return LocalDate.parse(dateTime, DateTimeFormatter.ofPattern(format));
	    } catch (RuntimeException e) {
	        return null;
	    }
	}
	
	/* Date to GMTString */
	public static String toGMTString(Date date) {
		SimpleDateFormat sdf_ymd = new SimpleDateFormat() ; 
		SimpleDateFormat sdf_hms = new SimpleDateFormat() ; 
		sdf_ymd.setTimeZone(new SimpleTimeZone(0, "KST")) ; 
		sdf_ymd.applyPattern("yyyy-MM-dd");
		sdf_hms.applyLocalizedPattern("HH:mm:ss.sss");
		sdf_hms.setTimeZone(new SimpleTimeZone(0, "KST")) ; 
		return sdf_ymd.format(date) + "T" + sdf_hms.format(date) + "Z"; 
	}
}
