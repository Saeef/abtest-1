package com.vuclip.abtesthttp.filter;

public class PagerContext {
	private static ThreadLocal<Integer> limit = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> start = new ThreadLocal<Integer>();
	
	/**
	 * 每页显示多数条
	 * @return
	 */
	public static int getLimit() {
		return limit.get();
	}
	/**
	 * 设置每页显示多少条信息
	 * @param _limit
	 */
	public static void setLimit(int _limit) {
		limit.set(_limit);
	}
	/**
	 * 移除对象
	 */
	public static void removeLimit() {
		limit.remove();
	}
	/**
	 * 获取limit的start的值
	 * @return
	 */
	public static int getStart() {
		return start.get();
	}
	/**
	 * 设置limit的start的值
	 * @param _start
	 */
	public static void setStart(int _start) {
		start.set(_start);
	}
	
	public static void removeStart() {
		start.remove();
	}
}
