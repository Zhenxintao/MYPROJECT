package com.bmts.heating.middleware.cache.services;

import com.bmts.heating.commons.utils.msmq.PointL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/7/21 16:01
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGrpc {

    @Autowired
    private RedisCacheService ldapCacheService;

    @Test
    public void testGrpc() throws IOException {
        long start = System.currentTimeMillis();
//        for (int i = 0; i < 40; i++) {
//            if (i % 2 == 0) {
//                List<PointL> pointByLdap = ldapCacheService.getPointByLdap("cn=PVSS1,cn=DeviceSet", 0, 15000);
//            } else if (i % 3 == 0) {
//                List<PointL> pointByLdap = ldapCacheService.getPointByLdap("cn=PVSS1,cn=DeviceSet", 15000, 25000);
//            } else if (i % 5 == 0) {
//                List<PointL> pointByLdap = ldapCacheService.getPointByLdap("cn=PVSS1,cn=DeviceSet", 25000, 35000);
//            } else {
//                List<PointL> pointByLdap = ldapCacheService.getPointByLdap("cn=PVSS1,cn=DeviceSet", 35000, 45000);
//            }
//            System.out.println(pointByLdap.size());
//            System.out.println(System.currentTimeMillis() - start);
//            System.out.println("------------------------------------------");
//        }

    }

}
