package com.vuclip.abtesthttp.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConvertUtil {
	/**
	 * 去除数组中重复信息
	 */
	public static String[] array_unique(String[] arr){
		List<String> list = new LinkedList<String>();
		for(int i=0;i<arr.length;i++){
			if(!list.contains(arr[i])){
				list.add(arr[i]);
			}
		}
		return (String[])list.toArray(new String[list.size()]);
	}
	
	public static String int2Str(int num){
		return String.valueOf(num);
	}
	
	/**
	 * 如果传进来数据能够转换成int类型，则返回转换后的数据类型，否则返回0
	 */
	public static int converterToInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 如果传进来数据能够转换成int类型，则返回转换后的数据类型，否则返回ret
	 */
	public static int converterToInt(String number, int ret) {
		try {
			return Integer.parseInt(number);
		} catch (Exception e) {
			return ret;
		}
	}
	public static String getString(String str){
		if(str == null){
			return "";
		}
		return str;
	}
	
	public static float converterToFloat(String str){
		try{
			return Float.parseFloat(str);
		}catch(Exception e){
			return 0;
		}
	}
	/**
	 * 把字符串中的空格删除以后，重新组成一个新的字符串
	 */
	public static String deleterBlank(String str){
		String s = "";
			for (int i = 0; i < str.length(); i++) {
				if(!(str.charAt(i)+"").equals(" ")){
					s += str.charAt(i);
				}
			}
			return s;
		}
	
	public static List<String> strToList(String[] ss){
		List<String> _ss = new ArrayList<String>();
		for (int i = 0; i < ss.length; i++) {
			_ss.add(ss[i]);
		}
		return _ss;
	}
	
	public static List<String> notInAnathor(List<String> l1, List<String> l2){
		List<String> notInSs2 = new ArrayList<String>();
		for (String string : l1) {
			if(!l2.contains(string)){
				notInSs2.add(string);
			}
		}
		return notInSs2;
	}
	/*全角 -> 半角*/
	public static String q2b(String input){
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(c[i] == '\u3000'){
				c[i] = ' ';
			}else if(c[i] > '\uFF00' && c[i] < '\uFF5F'){
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}
	/*半角转全角*/
	public static String b2q(String input){
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(c[i] == ' '){
				c[i] = '\u3000';
			}else if(c[i] < '\177'){
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	
	//判断ip是否在允许访问IP段范围内
	public static boolean ipIsValid(String ipSection,String ip){
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		int idx = ipSection.indexOf("-");
		if(!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))return false;
		String[] sips = ipSection.substring(0,idx).split("\\.");
		String[] sipe = ipSection.substring(idx+1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L,ipe = 0L,ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips<<8 | Integer.parseInt(sips[i]);
			ipe = ipe<<8 | Integer.parseInt(sipe[i]);
			ipt = ipt<<8 | Integer.parseInt(sipt[i]);
		}
		if(ips>ipe){
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return  ips <= ipt && ipt <= ipe;
	}
}
