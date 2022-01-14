package com.bmts.heating.bussiness.baseInformation.app.utils;

import java.util.Collection;

public class ListUtil {

	public static <T> boolean isValid(Collection<T> collection){
		return collection != null && collection.size()>0;
	}

}
