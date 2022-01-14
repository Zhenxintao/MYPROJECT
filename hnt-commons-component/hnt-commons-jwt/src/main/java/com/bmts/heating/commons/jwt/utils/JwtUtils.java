package com.bmts.heating.commons.jwt.utils;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import com.bmts.heating.commons.jwt.config.IgnoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;

@Slf4j
public class JwtUtils {
    /**
     * 密钥
     */
    //private static final String SECRET = "tscc_platform";
    private static final String SECRET = "tscc_sso";
    /**
     * 过期时间
     **/
    private static final long EXPIRATION = 36000L;//单位为秒
    /**
     * 生成用户token,设置token超时时间
     */

    /*public static String createToken(int userId, String userName) {
        Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        Date date=calendar.getTime();
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("user_id", userId)//userId
                .withClaim("user_name", userName)
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(date) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }*/
    public static String createToken(SysUser user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        Date date = calendar.getTime();
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("user_id", user.getCenterId())//userId user.getCenterId()
                .withClaim("user_name", user.getUsername())
                .withClaim("userinfo", JSONObject.toJSONString(user))
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(date) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    /**
     * 校验token并解析token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //解码异常则抛出异常
            log.error("validate token cause execption {}", e);
            return null;
        }
        return jwt.getClaims();
    }


    public static Integer getUserId(HttpServletRequest request) {

        try {
            Integer userId = Integer.valueOf(request.getAttribute("user_id").toString());//center_id
            return userId;
        } catch (ClassCastException e) {
            log.error("get userId cause execption {}",e);
            return null;
        }
    }

    public static String getUserName(HttpServletRequest request) {
        try {
            String userName = request.getAttribute("user_name").toString();
            return userName;

        } catch (ClassCastException e) {
            log.error("get userName cause execption {}",e);
            return "";
        }
    }

}
