package com.bmts.heating.service.task.transport.utils;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UrlConnUtil {
    /**
     * POST  请求方式
     *
     * @param path
     * @param data
     * @return
     */
    public static String postUrl(String path, String data) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(path);
        try {
            post.setEntity(new StringEntity(data));
            post.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(post);
            String res = null;
            if (response.getEntity() != null) {
                res = EntityUtils.toString(response.getEntity());
            }
            post.abort();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get 请求方式
     *
     * @param getUrl
     * @return
     */
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
