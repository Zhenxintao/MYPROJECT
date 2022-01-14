package com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UrlConn {
    /**
     * 采集和下发请求PVSS数据
     *
     * @param path
     * @param data
     * @return
     */
    public static String connUrl(String path, String data) {
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
}
