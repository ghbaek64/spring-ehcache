package org.syaku.util;

import java.util.*;

/**
 * 프로퍼티를 제어하는 유틸을 제공한다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http ://syaku.tistory.com
 * @since 16. 5. 25.
 */
public class PropertyUtils {

	/**
	 * 매개변수 target과 일치하는 프로퍼티ㄴ를 찾는 다.
	 *
	 * @param properties the properties
	 * @param target     the target
	 * @return the names
	 */
	public static List<String> getNames(Properties properties, String target) {
		List<String> result;

		Set<String> names = properties.stringPropertyNames();

		if (names == null) return null;

		result = new ArrayList<>();

		Iterator<String> itr = names.iterator();
		while(itr.hasNext()) {
			String name = itr.next();

			if (name == null) continue;

			if (!name.isEmpty() && name.startsWith(target)) {
				result.add(name);
			}
		}

		return result;
	}

	public static String[] getValues(String value) {
		return getValues(value, ",");
	}
	public static String[] getValues(String value, String delimiter) {
		if (value == null) return null;
		return value.split(delimiter);
	}
}
