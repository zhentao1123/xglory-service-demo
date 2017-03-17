package cn.xglory.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

/**
 * Date Util
 * @author zhangzhentao
 *
 */
public class DateUtil {
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取年
	 * @return
	 */
	public static int getYear(){
		return getYear(getNowDate());
	}
	
	/**
	 * 获取月
	 * @return
	 */
	public static int getMonth(){
		return getMonth(getNowDate());
	}
	
	/**
	 * 获取天
	 * @return
	 */
	public static int getDay(){
		return getDay(getNowDate());
	}
	
	/**
	 * 获取年
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		return getCalendar(date).get(Calendar.YEAR);
	}
	
	/**
	 * 获取月
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		return getCalendar(date).get(Calendar.MONTH)+1;
	}
	
	/**
	 * 获取天
	 * @param date
	 * @return
	 */
	public static int getDay(Date date){
		return getCalendar(date).get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date){
		return getStr(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 日期时间转字符串
	 * @param date
	 * @return
	 */
	public static String getDateTimeStr(Date date){
		return getStr(date, DEFAULT_DATETIME_FORMAT);
	}
	
	/**
	 * 日期时间转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getStr(Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param dateStr
	 * @return
	 * @throws ParseException 
	 */
	public static Date getDate(String dateStr) throws ParseException{
		return getDate(dateStr, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 字符串转日期时间
	 * @param dateTimeStr
	 * @return
	 * @throws ParseException 
	 */
	public static Date getDateTime(String dateTimeStr) throws ParseException{
		return getDate(dateTimeStr, DEFAULT_DATETIME_FORMAT);
	}
	
	/**
	 * 字符串转日期时间
	 * @param dateTimeStr
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date getDate(String dateTimeStr, String pattern) throws ParseException{
		return new SimpleDateFormat(pattern).parse(dateTimeStr);
	}
	
	/**
	 * 获取当前格式化日期
	 * @return
	 */
	public static String getNowDateStr(){
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(getNowDate());
	}
	
	/**
	 * 获取当前格式化日期时间
	 * @return
	 */
	public static String getNowDateTimeStr(){
		return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).format(getNowDate());
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static Date getNowDate(){
		return new Date();
	}
	
	/**
	 * 获取当前时间毫秒值
	 * @return
	 */
	public static long getNowTimeInMillis(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTimeInMillis();
	}
	
	/**
	 * 天数差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDaysDiff(Date date1, Date date2){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 自然天数差
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getNatureDaysDiff(Date date1, Date date2) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time1 = cal.getTimeInMillis();
		
		cal.setTime(date2);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time2 = cal.getTimeInMillis();
		
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 获取月天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month){
//		if (month > 12) return 0;
//		int[] monDays = new int[] { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
//		if (((year) % 4 == 0 && (year) % 100 != 0) || (year) % 400 == 0)
//			monDays[2]++;
//		return monDays[month];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取当前月天数
	 * @return
	 */
	public static int getMonthDays(){
		Calendar cal = Calendar.getInstance();
		return getMonthDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
	}
	
	/**
	 * 获取年天数
	 * @param year
	 * @return
	 */
	public static int getYearDays(int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取当前年天数
	 * @return
	 */
	public static int getYearDays(){
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取指定日期月份的最后一天
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		return cal.getTime();
	}
	
	/**
	 * 获取星期（英文格式）
	 * @param date
	 * @param isShort
	 * @return
	 */
	public static String getWeekDayStrEN(Date date, boolean isShort){
		DateTime dt = new DateTime(date);
		DateTime.Property pDoW = dt.dayOfWeek();
		if(isShort){
			return pDoW.getAsShortText(Locale.ENGLISH);
		}else{
			return pDoW.getAsText(Locale.ENGLISH);
		}
	}
	
	/**
	 * 获取星期（中文格式）
	 * @param date
	 * @return
	 */
	public static String getWeekDayStrCN(Date date){
		DateTime dt = new DateTime(date);
		DateTime.Property pDoW = dt.dayOfWeek();
		return pDoW.getAsText(Locale.CHINESE);
	}
	
	/**
	 * 获得指定日期与周一相差的天数
	 */
	public static int getMondayPlus(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		//获得今天是一周的第几天，星期日是第一天，星期一是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return 6;
		}else{
			return dayOfWeek - 2;
		}
	}

	/**
	 * 获取加上指定数量日期或时间单位后的日期
	 * @param date 为null则取当前日期
	 * @param unit Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND
	 * @param value 可为负数
	 * @return
	 */
	public static Date addDateTime(Date date, int unit, int value){
		Calendar cal = Calendar.getInstance();
		if(null!=date){
			cal.setTime(date);
		}
		cal.add(unit, value);
		return cal.getTime();
	}
	
	/**
	 * 获取加上指定数量日期单位后的日期
	 * @param dateStr 为null则取当前日期
	 * @param datePattern 为null则取默认格式
	 * @param unit Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR
	 * @param value 可为负数
	 * @return
	 * @throws ParseException
	 */
	public static String addDate(String dateStr, String datePattern, int unit, int value) throws ParseException{
		Date date = null;
		if(null!=dateStr){
			if(null!=datePattern){
				date = new SimpleDateFormat(datePattern).parse(dateStr);
			}else{
				date = new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateStr);
			}
		}else{
			date = new Date();
		}
		Date resultDate = addDateTime(date, unit, value);
		return new SimpleDateFormat(datePattern).format(resultDate);
	}
	
	/**
	 * 获取加上指定数量时间单位后的日期
	 * @param dateStr 为null则取当前时间
	 * @param datePattern 为null则取默认格式
	 * @param unit Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND
	 * @param value 可为负数
	 * @return
	 * @throws ParseException
	 */
	public static String addTime(String dateStr, String datePattern, int unit, int value) throws ParseException{
		Date date = null;
		if(null!=dateStr){
			if(null!=datePattern){
				date = new SimpleDateFormat(datePattern).parse(dateStr);
			}else{
				date = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).parse(dateStr);
			}
		}else{
			date = new Date();
		}
		Date resultDate = addDateTime(date, unit, value);
		return new SimpleDateFormat(datePattern).format(resultDate);
	}
	
	private static Calendar getCalendar(){
		return Calendar.getInstance();
	}
	
	private static Calendar getCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static void main(String[] args) throws ParseException{
		System.out.println(DateUtil.getMonthDays(2000, 2));
	}
}
