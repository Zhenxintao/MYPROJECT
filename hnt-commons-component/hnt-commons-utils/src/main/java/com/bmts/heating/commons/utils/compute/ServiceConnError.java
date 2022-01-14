package com.bmts.heating.commons.utils.compute;

public class ServiceConnError {

    public static boolean checkError(Throwable throwable){
        Throwable error= throwable.getCause();
        if(error instanceof java.net.ConnectException){
            return true;
        } else if(error instanceof java.net.SocketException){
            return true;
        } else {
            return false;
        }
    }
}
