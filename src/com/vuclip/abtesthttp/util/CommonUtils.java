package com.vuclip.abtesthttp.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.log4j.Logger;
public class CommonUtils {
	
	private static final Logger logger = Logger.getLogger(CommonUtils.class);
	public static String[] distinct(String[] input) {
		if (input == null)
			return null;
		List list = Arrays.asList(input);
		Set set = new HashSet(list);
		String[] result = (String[]) set.toArray(new String[0]);
		return result;
	}

	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}
	
	public static String getDateValue(String dateFormat, long milliseconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		return simpleDateFormat.format(new Date(milliseconds));
	}
	
	public static long getCurrentDayOfMonth() {
		Calendar a = Calendar.getInstance();
		a.setTime(new Date());
		a.set(Calendar.HOUR_OF_DAY, 0);
		a.set(Calendar.MINUTE, 0);
		a.set(Calendar.SECOND, 0);
		return a.getTimeInMillis();
	}
	
	public static long getSpecifiedMonth(Date date, int which) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, 1); // from September 1 00:00:00
		a.add(Calendar.MONTH, 1); 
		a.add(Calendar.MONTH, which);
		a.set(Calendar.HOUR_OF_DAY, 0);
		a.set(Calendar.MINUTE, 0);
		a.set(Calendar.SECOND, 0);
		a.set(Calendar.MILLISECOND, 0);
		return a.getTimeInMillis();
	}
    public static void main(String[] args) {
        System.out.println(string2MD5("Vuclip@23421"));
        System.out.println(getDateValue("MM|yyyy", System.currentTimeMillis()));
    }
}
