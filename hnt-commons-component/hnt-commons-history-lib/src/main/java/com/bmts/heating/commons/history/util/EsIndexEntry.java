package com.bmts.heating.commons.history.util;

//索引指定类
public class EsIndexEntry {

	private static String suffix;

	public static void setSuffix(String suffix) {
		EsIndexEntry.suffix = suffix;
	}

	public static String getSuffix() {
		return suffix;
	}

}
