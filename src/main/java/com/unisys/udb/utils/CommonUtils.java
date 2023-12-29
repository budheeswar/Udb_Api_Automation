package com.unisys.udb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils {

	public static String getFormatedDate(String format) {
		String sDateFormat;
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		Calendar currentDate = Calendar.getInstance();
		sDateFormat = sformat.format(currentDate.getTime());
		return sDateFormat;
	}
}
