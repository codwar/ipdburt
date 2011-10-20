package iddb.legacy.python.date;

import java.util.Date;

public final class DateUtils {

	public static Date timestampToDate(Long value) {
		return new Date(value * 1000L);
	}
	
	public static Long dateToTimestamp(Date value) {
		return value.getTime() / 1000L;
	}
	
}
