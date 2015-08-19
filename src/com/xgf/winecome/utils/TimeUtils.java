package com.xgf.winecome.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static final String FORMAT_PATTERN_DATE = "yyyy-MM-dd HH:mm:ss";

	public static long dateToLong(Date date) {

		return date.getTime() / 1000; // 得到秒数，Date类型的getTime()返回毫秒数
	}

	/**
	 * 将Unix时间戳改成正常时间
	 * 
	 * @param timestampString
	 *            Unix时间戳
	 * @param pattern
	 *            要显示的日期的格式，例如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String TimeStamp2Date(String timestampString, String pattern) {
		if (timestampString != null && !"".equals(timestampString)) {
			Long timestamp = Long.parseLong(timestampString);
			String date = new java.text.SimpleDateFormat(pattern)
					.format(new java.util.Date(timestamp));
			return date;
		}
		return "";
	}

	public static long dateToLong(String date, String pattern) {
		long time;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date dt2 = null;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		time = dt2.getTime() / 1000;
		return time; // 得到秒数，Date类型的getTime()返回毫秒数
	}

	// a integer to xx:xx:xx
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

}
