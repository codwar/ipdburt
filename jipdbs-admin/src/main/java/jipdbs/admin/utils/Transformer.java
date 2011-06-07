package jipdbs.admin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Transformer {

	private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	public static String date_to_string(Date date) {
		if (date == null) return null;
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.format(date);
	}

	public static Date string_to_date(String str) {
		if (str == null || str.equals("\"null\"")) return null;
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static final String string_to_null(String str) {
		if (str.equals("\"null\"")) return null;
		return str;
	}
	
	public static final Long string_to_long(String str) {
		String s = string_to_null(str);
		if (s != null) {
			return Long.parseLong(s);
		}
		return null;
	}
	
}
