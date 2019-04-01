package io.vicp.wloves.cache.redis.util;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 判断字符串是否不为null或去空格后长度为0
	 * 
	 * @param s
	 *            字符串
	 * @return true，不为null或去空格后长度为0
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}

	/**
	 * 判断至少一个字符串是否不为null或去空格后长度为0
	 * 
	 * @param ss
	 *            字符串数组
	 * @return 如果至少一个字符串不为null或去空格后长度为0，则返回true。
	 */
	public static boolean isNotEmptyAny(String... ss) {
		for (String s : ss) {
			if (isNotEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断所有字符串是否不为null或去空格后长度为0
	 * 
	 * @param ss
	 *            字符串数组
	 * @return 如果所有字符串不为null或去空格后长度为0，则返回true。
	 */
	public static boolean isNotEmptyAll(String... ss) {
		for (String s : ss) {
			if (isEmpty(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 把一个对象数组用分隔字符串连接成一个字符串。
	 * 
	 * @param objs
	 *            对象数组
	 * @param splitString
	 *            分割字符串
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] objs, String splitString) {
		StringBuilder s = new StringBuilder();
		if (objs.length > 0) {
			s.append(objs[0]);
			for (int i = 1; i < objs.length; i++) {
				s.append(splitString).append(objs[i]);
			}
		}

		return s.toString();
	}

	/**
	 * 把一个对象列表用分隔字符串连接成一个字符串。
	 * 
	 * @param objList
	 *            对象列表
	 * @param splitString
	 *            分割字符串
	 * @return 连接后的字符串
	 */
	public static String join(List<?> objList, String splitString) {
		StringBuilder s = new StringBuilder();
		if (objList.size() > 0) {
			s.append(objList.get(0));
			for (int i = 1, ii = objList.size(); i < ii; i++) {
				s.append(splitString).append(objList.get(i));
			}
		}

		return s.toString();
	}

	/**
	 * 把一个对象数组的列表的某一列数据用分隔字符串连接成一个字符串。
	 * 
	 * @param objList
	 *            对象数组的列表
	 * @param splitString
	 *            分割字符串
	 * @return 连接后的字符串
	 */
	public static <T> String join(List<T[]> objList, int columnIndex,
			String splitString) {
		StringBuilder s = new StringBuilder();
		if (objList.size() > 0) {
			s.append(objList.get(0)[columnIndex]);
			for (int i = 1, ii = objList.size(); i < ii; i++) {
				s.append(splitString).append(objList.get(i)[columnIndex]);
			}
		}

		return s.toString();
	}

	/**
	 * 判断字符串是否为null或去空格后长度为0
	 * 
	 * @param s
	 *            字符串
	 * @return true，为null或去空格后长度为0
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 是否是邮件地址格式
	 * 
	 * @param s
	 *            字符串
	 * @return true，是
	 */
	public static boolean isEmail(String s) {
		return s.matches("^[a-zA-Z0-9._-]+@([a-zA-Z0-9_-])+(\\.[a-zA-Z0-9_-]+)+$");
	}

	/**
	 * 只含有汉字、数字、字母、下划线不能以下划线开头和结尾
	 *
	 * @param s 字符串
	 * @return true，是
	 */
	public static boolean isName(String s) {
		return s.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
	}

	/**
	 * 是否是手機號碼格式
	 * 
	 * @param s
	 *            字符串
	 * @return true，是
	 */
	public static boolean isMobile(String s) {
		if (isEmpty(s)) {
			return false;
		}
		// 增加虚拟运营商号码段..
		return s.matches("^1[34578]\\d{9}$");
	}

	/**
	 * 创建32位UUID
	 * 
	 * @return true，是
	 */
	public static String getTWFormatUUID() {
		return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}

	public static String encryptMobile(String mobile) {
		if (mobile == null) {
			return null;
		}
		int encryptLength = mobile.length() > 4 ? mobile.length() - 4 : mobile
				.length() / 2;
		StringBuilder sb = new StringBuilder(mobile.length());
		for (int i = 0; i < encryptLength; i++) {
			sb.append("*");
		}
		sb.append(mobile.substring(encryptLength));
		return sb.toString();
	}

	public static String encryptEmail(String email) {
		if (email == null) {
			return null;
		}

		String[] split = email.split("@");
		if (split.length != 2) {
			return null;
		}

		String username = split[0];
		String mailTail = split[1];

		int encryptLength = username.length() > 4 ? username.length() - 4
				: username.length() / 2;
		encryptLength = encryptLength == 0 ? 1 : encryptLength;

		StringBuilder sb = new StringBuilder(username.length());

		sb.append(username.substring(0, username.length() - encryptLength));

		for (int i = 0; i < encryptLength; i++) {
			sb.append("*");
		}
		sb.append("@");
		sb.append(mailTail);

		return sb.toString();

	}

	/**
	 * @功能 将英文字符串首字母转为大写
	 * @param str
	 *            要转换的字符串
	 * @return String 型值
	 */
	public static String toUpCaseFirst(String str) {
		if (str == null || "".equals(str)) {
			return str;
		} else {
			char[] temp = str.toCharArray();
			temp[0] = str.toUpperCase().toCharArray()[0];
			str = String.valueOf(temp);
		}

		return str;
	}

	public static String polishZero(String str) {
//		if (str.length() >= 32)
//			return str;
//		String polish = "00000000000000000000000000000000";
//		return polish.substring(str.length()) + str;
		
		return polishZero(str, 32);
	}

	public static String polishZero(String str, int length) {
		if (str.length() >= length)
			return str;
		int size = length - str.length();
		StringBuffer polish = new StringBuffer();
		for (int i = 0; i < size; i++) {
			polish.append("0");
		}
		return polish + str;
	}
	

	/**
	 * Mac 格式校验
	 * 
	 * @param mac
	 * @return
	 */
	public static Boolean isMac(String mac) {
		if (isEmpty(mac)) {
			return false;
		}

		Pattern p = Pattern.compile("^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$");

		Matcher m = p.matcher(mac);

		if (!m.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 是不是一个软件版本
	 *
	 * @param s
	 * @return
	 */
	public static boolean isSoftwareVer(String s) {
		return s.matches("\\d{1,5}\\.\\d{1,5}\\.\\d{1,5}");
	}

	/**
	 * 是不是一个软件名字
	 * 只能含有数字、字母、下划线，不能以下划线开头和结尾
	 * @param s
	 * @return
	 */
	public static boolean isSoftwareName(String s) {
		return s.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$");
	}


	/**
	 * 日期时间
	 *
	 * 格式201512121212
	 * @param s
	 * @return
	 */
	public static boolean isYYMMDDHHMM(String s) {
		return s.matches("((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))(([0-1]?[0-9]|2[0-3])([0-5][0-9])$)");
	}
	
	/**
	 * 判断两个字符串是否相等
	 * @author zhengwei.zhu
	 * @date 2018年4月27日 下午3:37:58
	 * @param str1
	 * @param str2
	 * @return true代表相等，false代表不相等
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		} 
		return str1.equals(str2);
	}
}
