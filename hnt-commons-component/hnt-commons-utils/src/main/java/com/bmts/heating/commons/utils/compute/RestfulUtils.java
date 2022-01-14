package com.bmts.heating.commons.utils.compute;

public class RestfulUtils {

    public static String resolutionAddress(String host, String port){
        String url="http://"+host+":"+port;
        return url;
    }
}
