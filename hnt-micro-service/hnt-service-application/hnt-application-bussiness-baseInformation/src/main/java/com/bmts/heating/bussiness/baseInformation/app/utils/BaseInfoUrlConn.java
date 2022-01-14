package com.bmts.heating.bussiness.baseInformation.app.utils;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BaseInfoUrlConn {

    public static String doGet(String getUrl) {
        String res = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(getUrl);
            HttpResponse response = client.execute(get);
            if (response.getEntity() != null) {
                res = EntityUtils.toString(response.getEntity());
            }
            get.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
