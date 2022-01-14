package com.bmts.heating.bussiness.baseInformation.app.ann;

import com.bmts.heating.commons.redis.utils.RedisKeys;

import java.lang.annotation.*;

/**
 * @ClassName ClearCache
 * @Author naming
 * @Date 2020/11/18
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ClearCache {
    RedisKeys key() default RedisKeys.FIRST_BASE;
}
