package com.bmts.heating.commons.history.pojo;

import java.util.Date;
import java.util.UUID;

public class UUIDUtil {


	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}


	public static long getLongId(int... ints){
		StringBuilder stringBuilder = new StringBuilder(String.valueOf(new Date().getTime()));
		for (int anInt : ints) {
			stringBuilder.append(anInt);
		}
		return Long.parseLong(stringBuilder.toString());
	}

//	public static void main(String[] args) {
//		System.out.println(getLongId(1, 2));
//	}
}
