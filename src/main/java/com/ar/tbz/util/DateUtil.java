package com.ar.tbz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static final String FULL_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	public static String PATTERN_Y4_M2_D2 = "yyyy-MM-dd";
	public static String PATTERN_D2_M2_Y4_H_m2 = "yyyy-MM-dd HH:mm";

	public static String formatSdf(String pattern, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date formatParse(String pattern, String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(fecha);
	}

	public static Date currentDate() {
		long ahoraLong = System.currentTimeMillis();
		Date date = new Date();
		date.setTime(ahoraLong);
		return date;
	}

	public static String currentDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat(FULL_TIMESTAMP_PATTERN);
		return sdf.format(new Date());
	}
}
