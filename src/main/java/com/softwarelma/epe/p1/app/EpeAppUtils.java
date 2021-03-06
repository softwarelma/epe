package com.softwarelma.epe.p1.app;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class EpeAppUtils {

	public static void checkNull(String name, String value) throws EpeAppException {
		if (name == null) {
			throw new EpeAppException("Null name");
		}

		if (value == null) {
			throw new EpeAppException("Null " + name);
		}
	}

	public static void checkNull(String name, Object value) throws EpeAppException {
		checkNull("name", name);

		if (value == null) {
			throw new EpeAppException("Null " + name);
		}
	}

	public static void checkNullForceNull(String name, String value) throws EpeAppException {
		checkNull("name", name);

		if (value != null) {
			throw new EpeAppException("Not null " + name);
		}
	}

	public static void checkNullForceNull(String name, String value, String message) throws EpeAppException {
		checkNull("name", name);

		if (value != null) {
			throw new EpeAppException(message);
		}
	}

	public static void checkNullForceNull(String name, Object value) throws EpeAppException {
		checkNull("name", name);

		if (value != null) {
			throw new EpeAppException("Not null " + name);
		}
	}

	public static void checkNullForceNull(String name, Object value, String message) throws EpeAppException {
		checkNull("name", name);

		if (value != null) {
			throw new EpeAppException(message);
		}
	}

	public static void checkBooleanForceTrue(String name, boolean b) throws EpeAppException {
		checkNull("name", name);

		if (!b) {
			throw new EpeAppException("Found false in " + name);
		}
	}

	public static void checkBooleanForceTrue(String name, boolean b, String message) throws EpeAppException {
		checkNull("name", name);

		if (!b) {
			throw new EpeAppException(message);
		}
	}

	public static void checkBooleanForceFalse(String name, boolean b) throws EpeAppException {
		checkNull("name", name);

		if (b) {
			throw new EpeAppException("Found true in " + name);
		}
	}

	public static void checkBooleanForceFalse(String name, boolean b, String message) throws EpeAppException {
		checkNull("name", name);

		if (b) {
			throw new EpeAppException(message);
		}
	}

	public static void checkMinorOrEqual(int value1, int value2) throws EpeAppException {
		if (value1 > value2) {
			throw new EpeAppException("Value " + value1 + " should be <= " + value2);
		}
	}

	public static void checkEquals(String name1, String name2, Object value1, Object value2) throws EpeAppException {
		checkEquals(name1, name2, value1, value2, null);
	}

	public static void checkEquals(String name1, String name2, Object value1, Object value2, String message) throws EpeAppException {
		checkNull("name1", name1);
		checkNull("name2", name2);

		if (value1 == null && value2 == null) {
			return;
		}

		if (value1 == null || value2 == null || !value1.equals(value2)) {
			throw new EpeAppException(message == null ? "Vars " + name1 + " and " + name2 + " are not equals" : message);
		}
	}

	public static void checkEquals(String name1, String name2, List<String> list, Set<String> set) throws EpeAppException {
		checkNull("name1", name1);
		checkNull("name2", name2);

		if (list == null && set == null) {
			return;
		}

		if (list.isEmpty() && set.isEmpty()) {
			return;
		}

		if (list.size() != set.size()) {
			throw new EpeAppException("Vars " + name1 + " and " + name2 + " are not equals, list size " + list.size() + " != set size " + set.size());
		}

		List<String> listTmp = new ArrayList<>(list);

		for (String str : set) {
			if (!listTmp.remove(str)) {
				throw new EpeAppException("Vars " + name1 + " and " + name2 + " are not equals, the list does not contain \"" + str + "\"");
			}
		}
	}

	public static void checkContains(Set<String> set, String name, String value) throws EpeAppException {
		checkNull("name", name);
		checkNull("set", set);

		if (!set.contains(value)) {
			throw new EpeAppException(name + " \"" + value + "\" is not contained in " + set);
		}
	}

	public static void checkContains(List<String> list, String name, String value) throws EpeAppException {
		checkNull("name", name);
		checkNull("list", list);

		if (!list.contains(value)) {
			throw new EpeAppException(name + " \"" + value + "\" is not contained in " + list);
		}
	}

	public static void checkContains(String[] array, String name, String value) throws EpeAppException {
		checkNull("name", name);
		checkNull("array", array);
		List<String> list = Arrays.asList(array);
		checkContains(list, name, value);
	}

	public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1) throws EpeAppException {
		checkRange(i, i0, i1, open0, open1, "");
	}

	public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1, String postMessage) throws EpeAppException {
		checkRange(i, i0, i1, open0, open1, "", postMessage);
	}

	public static void checkRange(int i, int i0, int i1, boolean open0, boolean open1, String paramName, String postMessage) throws EpeAppException {
		paramName = paramName == null ? "" : paramName + " ";
		postMessage = postMessage == null ? "" : postMessage;

		if (open0) {
			if (i < i0) {
				throw new EpeAppException("Out of range, the param " + paramName + " (" + i + ") is < " + i0 + ". " + postMessage);
			} else if (i == i0) {
				throw new EpeAppException("Out of range, the param " + paramName + "(" + i + ") is == " + i0 + ". " + postMessage);
			}
		} else {
			if (i < i0) {
				throw new EpeAppException("Out of range, the param " + paramName + "(" + i + ") is < " + i0 + ". " + postMessage);
			}
		}

		if (open1) {
			if (i > i1) {
				throw new EpeAppException("Out of range, the param " + paramName + "(" + i + ") is > " + i1 + ". " + postMessage);
			} else if (i == i1) {
				throw new EpeAppException("Out of range, the param " + paramName + "(" + i + ") is == " + i1 + ". " + postMessage);
			}
		} else {
			if (i > i1) {
				throw new EpeAppException("Out of range, the param " + paramName + "(" + i + ") is > " + i1 + ". " + postMessage);
			}
		}
	}

	public static <T> void checkSize(String name, List<T> list, int size) throws EpeAppException {
		checkNull(name, list);

		if (list.size() != size) {
			throw new EpeAppException("Wrong list size: " + list.size() + " != " + size);
		}
	}

	public static void checkEmpty(String paramName, Object paramValue) throws EpeAppException {
		checkEmpty(paramName, paramValue, "The param ", " cannot be empty");
	}

	public static void checkEmptyIT(String paramName, Object paramValue) throws EpeAppException {
		checkEmpty(paramName, paramValue, "Il parametro ", " non puo' essere vuoto");
	}

	public static void checkEmpty(String paramName, Object paramValue, String theParam, String cannotBeEmpty) throws EpeAppException {
		checkNull("paramName", paramName);
		theParam = theParam == null ? "The param " : theParam;
		cannotBeEmpty = cannotBeEmpty == null ? " cannot be empty" : cannotBeEmpty;

		if (paramValue == null || paramValue.toString().trim().isEmpty()) {
			throw new EpeAppException(theParam + paramName + cannotBeEmpty);
		}
	}

	public static void checkEmptyNoTrim(String paramName, Object paramValue) throws EpeAppException {
		checkEmptyNoTrim(paramName, paramValue, "The param ", " cannot be empty");
	}

	public static void checkEmptyNoTrimIT(String paramName, Object paramValue) throws EpeAppException {
		checkEmptyNoTrim(paramName, paramValue, "Il parametro ", " non puo' essere vuoto");
	}

	public static void checkEmptyNoTrim(String paramName, Object paramValue, String theParam, String cannotBeEmpty) throws EpeAppException {
		checkNull("paramName", paramName);
		theParam = theParam == null ? "The param " : theParam;
		cannotBeEmpty = cannotBeEmpty == null ? " cannot be empty" : cannotBeEmpty;

		if (paramValue == null || paramValue.toString().isEmpty()) {
			throw new EpeAppException(theParam + paramName + cannotBeEmpty);
		}
	}

	public static void checkEmptyForceEmpty(String paramName, Object paramValue) throws EpeAppException {
		checkEmptyForceEmpty(paramName, paramValue, "The param ", " must be empty, found: ");
	}

	public static void checkEmptyForceEmptyIT(String paramName, Object paramValue) throws EpeAppException {
		checkEmptyForceEmpty(paramName, paramValue, "Il parametro ", " dev'essere vuoto, trovato: ");
	}

	public static void checkEmptyForceEmpty(String paramName, Object paramValue, String theParam, String mustBeEmpty) throws EpeAppException {
		checkNull("paramName", paramName);
		theParam = theParam == null ? "The param " : theParam;
		mustBeEmpty = mustBeEmpty == null ? " must be empty, found: " : mustBeEmpty;

		if (paramValue != null && !paramValue.toString().trim().isEmpty()) {
			throw new EpeAppException(theParam + paramName + mustBeEmpty + paramValue);
		}
	}

	public static <T> void checkEmptyArray(String paramName, T[] paramValue) throws EpeAppException {
		checkEmptyArray(paramName, paramValue, "The param ", " cannot be empty");
	}

	public static <T> void checkEmptyArrayIT(String paramName, T[] paramValue) throws EpeAppException {
		checkEmptyArray(paramName, paramValue, "Il parametro ", " non puo' essere vuoto");
	}

	public static <T> void checkEmptyArray(String paramName, T[] paramValue, String theParam, String cannotBeEmpty) throws EpeAppException {
		checkEmpty("paramName", paramName);
		theParam = theParam == null ? "The param " : theParam;
		cannotBeEmpty = cannotBeEmpty == null ? " cannot be empty" : cannotBeEmpty;

		if (paramValue == null || paramValue.length == 0) {
			throw new EpeAppException(theParam + paramName + cannotBeEmpty);
		}
	}

	public static <T> void checkNullListContent(String paramName, List<T> paramValue) throws EpeAppException {
		checkNull(paramName, paramValue);

		for (int i = 0; i < paramValue.size(); i++) {
			T t = paramValue.get(i);
			checkNull(paramName + "[" + i + "]", t);
		}
	}

	public static <T> void checkEmptyListContent(String paramName, List<T> paramValue) throws EpeAppException {
		checkEmptyList(paramName, paramValue, "The param ", " cannot be empty");

		for (int i = 0; i < paramValue.size(); i++) {
			T t = paramValue.get(i);
			checkEmpty(paramName + "[" + i + "]", t);
		}
	}

	public static <T> void checkEmptyList(String paramName, List<T> paramValue) throws EpeAppException {
		checkEmptyList(paramName, paramValue, "The param ", " cannot be empty");
	}

	public static <T> void checkEmptyListIT(String paramName, List<T> paramValue) throws EpeAppException {
		checkEmptyList(paramName, paramValue, "Il parametro ", " non puo' essere vuoto");
	}

	public static <T> void checkEmptyList(String paramName, List<T> paramValue, String theParam, String cannotBeEmpty) throws EpeAppException {
		checkNull("paramName", paramName);
		theParam = theParam == null ? "The param " : theParam;
		cannotBeEmpty = cannotBeEmpty == null ? " cannot be empty" : cannotBeEmpty;

		if (paramValue == null || paramValue.isEmpty()) {
			throw new EpeAppException(theParam + paramName + cannotBeEmpty);
		}
	}

	public static <K, V> void checkEmptyMap(String paramName, Map<K, V> paramValue) throws EpeAppException {
		checkNull("paramName", paramName);
		checkNull(paramName, paramValue);

		if (paramValue.isEmpty()) {
			throw new EpeAppException("Empty " + paramName);
		}
	}

	public static void checkDir(File dir) throws EpeAppException {
		checkNull("dir", dir);

		if (!dir.isDirectory()) {
			throw new EpeAppException("The file \"" + dir.getName() + "\" is not a dir");
		}
	}

	public static void checkInstanceOf(String name, Object value, Class<?> clazz) throws EpeAppException {
		EpeAppUtils.checkNull("name", name);
		// value can be null
		EpeAppUtils.checkNull("clazz", clazz);

		if (value != null && !clazz.isInstance(value)) {
			throw new EpeAppException("Invalid class for " + name + ", required " + clazz.getName() + ", found " + value.getClass().getName());
		}
	}

	public static <T> boolean isEmptyArray(T[] param) throws EpeAppException {
		return param == null || param.length == 0;
	}

	public static <T> boolean isEmptyList(List<T> param) throws EpeAppException {
		return param == null || param.isEmpty();
	}

	/**
	 * it must be negative
	 */
	public static void checkNegative(int param) throws EpeAppException {
		if (param >= 0)
			throw new EpeAppException("Expected < 0, found: " + param);
	}

	/**
	 * it must be negative
	 */
	public static void checkNotNegative(int param) throws EpeAppException {
		if (param < 0)
			throw new EpeAppException("Expected >= 0, found: " + param);
	}

	/**
	 * it must be positive
	 */
	public static void checkPositive(int param) throws EpeAppException {
		if (param <= 0)
			throw new EpeAppException("Expected > 0, found: " + param);
	}

	/**
	 * it must be not positive
	 */
	public static void checkNotPositive(int param) throws EpeAppException {
		if (param > 0)
			throw new EpeAppException("Expected <= 0, found: " + param);
	}

	public static boolean isEmpty(Integer param) {
		return param == null || param.intValue() == 0;
	}

	public static boolean isEmpty(String param) {
		return param == null || param.isEmpty();
	}

	public static boolean isEmptyTrimming(String param) {
		return param == null || param.isEmpty() || param.trim().isEmpty() || param.trim().replace("\t", "").replace("\r\n", "").replace("\n", "").isEmpty();
	}

	public static String retrieveVisualTrim(String param) {
		if (param == null || param.isEmpty())
			return param;
		int i0 = 0;
		int i1 = param.length();
		char c;

		for (int i = 0; i < param.length() + 1; i++) {
			i0 = i;
			if (i0 == param.length())
				break;
			c = param.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
				break;
		}

		for (int i = param.length(); i > 0 && i >= i0; i--) {
			i1 = i;
			c = param.charAt(i - 1);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
				break;
		}

		return param.substring(i0, i1);
	}

	public static String getNotContainedString(String text, String open, String close) throws EpeAppException {
		EpeAppUtils.checkNull("text", text);
		EpeAppUtils.checkNull("open", open);
		EpeAppUtils.checkNull("close", close);
		int i = 0;

		while (text.contains(open + i + close)) {
			i++;
		}

		return open + i + close;
	}

	public static String getNotContainedString(String text) throws EpeAppException {
		return getNotContainedString(text, EpeAppConstants.CONTAINED_STRING_OPEN, EpeAppConstants.CONTAINED_STRING_CLOSE);
	}

	public static String getNotContainedString(String notContained, int iter, String close) throws EpeAppException {
		EpeAppUtils.checkNull("notContained", notContained);
		notContained = notContained.substring(0, notContained.length() - 1) + "-" + iter + close;
		return notContained;
	}

	public static String getNotContainedString(String notContained, int iter) throws EpeAppException {
		return getNotContainedString(notContained, iter, EpeAppConstants.CONTAINED_STRING_CLOSE);
	}

	public static String cleanFilename(String filename) throws EpeAppException {
		checkNull("filename", filename);
		return filename.replace("\\", "/");
	}

	public static String cleanDirName(String dirName) throws EpeAppException {
		checkNull("dirName", dirName);
		dirName = cleanFilename(dirName);
		dirName = dirName.endsWith("/") || dirName.isEmpty() ? dirName : dirName + "/";
		return dirName;
	}

	public static Map.Entry<String, String> retrieveKeyAndValue(String fullprop) throws EpeAppException {
		checkEmpty("fullprop", fullprop);
		if (!fullprop.contains("="))
			throw new EpeAppException("fullprop not valid \"" + fullprop + "\", should be like key=value");
		String key = fullprop.substring(0, fullprop.indexOf("="));
		String value = fullprop.substring(fullprop.indexOf("=") + 1);
		Map.Entry<String, String> keyValue = new AbstractMap.SimpleEntry<>(key, value);
		return keyValue;
	}

	public static Map.Entry<String, String> retrieveKeyAndValueVisualTrim(String fullprop) throws EpeAppException {
		Map.Entry<String, String> keyValue = retrieveKeyAndValue(fullprop);
		Map.Entry<String, String> keyValueVisualTrim = new AbstractMap.SimpleEntry<>(retrieveVisualTrim(keyValue.getKey()),
				retrieveVisualTrim(keyValue.getValue()));
		return keyValueVisualTrim;
	}

	public static Map.Entry<String, String> retrieveFilePathAndName(String fullFileName) throws EpeAppException {
		return retrievePathAndLast(fullFileName, "/");
	}

	public static Map.Entry<String, String> retrievePathAndLast(String fullPath, String separator) throws EpeAppException {
		checkEmpty("fullPath", fullPath);
		fullPath = cleanFilename(fullPath);
		fullPath = fullPath.endsWith(separator) ? fullPath.substring(0, fullPath.length() - 1) : fullPath;
		String[] array = fullPath.split(separator);

		String name = array[array.length - 1];
		String path = fullPath.substring(0, fullPath.length() - name.length());

		Map.Entry<String, String> filePathAndLast = new AbstractMap.SimpleEntry<>(path, name);
		return filePathAndLast;
	}

	public static String replaceNotContainedWithReplaced(String sentStr, Map<String, String> mapNotContainedReplaced) {
		for (String notContained : mapNotContainedReplaced.keySet()) {
			sentStr = sentStr.replace(notContained, mapNotContainedReplaced.get(notContained));
		}

		return sentStr;
	}

	public static String retrieveTimestamp(String format) throws EpeAppException {
		checkEmpty("format", format);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.trim());
		return simpleDateFormat.format(new Date());
		// DateTimeFormatter formatter =
		// DateTimeFormatter.ofPattern(format.trim());
		// return LocalDateTime.now().format(formatter);
	}

	public static String retrieveTimestamp(String format, Date date) throws EpeAppException {
		checkEmpty("format", format);
		checkNull("date", date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.trim());
		return simpleDateFormat.format(date);
	}

	public static long parseLong(String s) throws EpeAppException {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new EpeAppException("Invalid long " + s, e);
		}
	}

	public static int parseInt(String str) throws EpeAppException {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new EpeAppException("Invalid int " + str, e);
		}
	}

	public static boolean parseBoolean(String str) throws EpeAppException {
		checkContains(new String[] { "true", "false" }, "str", str);
		return Boolean.parseBoolean(str);
	}

	public static Date parseTimestampToDate(String format, String timestampStr) throws EpeAppException {
		checkEmpty("format", format);
		checkEmpty("timestampStr", timestampStr);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.trim());

		try {
			return simpleDateFormat.parse(timestampStr);
		} catch (ParseException e) {
			throw new EpeAppException("Invalid timestamp string " + timestampStr, e);
		}
	}

	public static Timestamp parseTimestampToTimestamp(String format, String timestampStr) throws EpeAppException {
		checkEmpty("format", format);
		checkEmpty("timestampStr", timestampStr);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.trim());

		try {
			Date date = simpleDateFormat.parse(timestampStr);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			throw new EpeAppException("Invalid timestamp string " + timestampStr, e);
		}
	}

	public static <T> List<T> asList(T[] array) {
		List<T> list = new ArrayList<T>();

		for (T t : array) {
			list.add(t);
		}

		return list;
	}

	public static void checkForceLower(String name, String value) throws EpeAppException {
		EpeAppUtils.checkNull("name", name);
		EpeAppUtils.checkNull("value", value);

		if (!value.toLowerCase().equals(value)) {
			throw new EpeAppException("The param " + name + " is not in lower case, found: " + value);
		}
	}

	public static void checkForceUpper(String name, String value) throws EpeAppException {
		EpeAppUtils.checkNull("name", name);
		EpeAppUtils.checkNull("value", value);

		if (!value.toUpperCase().equals(value)) {
			throw new EpeAppException("The param " + name + " is not in upper case, found: " + value);
		}
	}

}
