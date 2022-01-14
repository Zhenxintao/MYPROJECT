package com.bmts.heating.bussiness.baseInformation.app.aspect;

import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.redis.utils.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CacheAspect
 * @Author naming
 * @Date 2020/11/18
 **/
@Component
@Aspect
@Slf4j
public class CacheAspect {
    @Autowired
    RedisManager redisManager;

    @Pointcut("@annotation(com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache)")
    public void targetAspec() {

    }
    @After(value = "targetAspec()")
    public void after(JoinPoint joinPoint) {
        try {

            ClearCache annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ClearCache.class);
            RedisKeys key = annotation.key();
            switch (key) {
                case FIRST_BASE: {
                    if (redisManager.hasKey(RedisKeyConst.FIRST_BASE_DATA))
                        redisManager.del(RedisKeyConst.FIRST_BASE_DATA);
                }
            }
        } catch (Exception e) {
            log.error("aspec delete redis cause exception {}", e.getMessage());
        }
    }
}
