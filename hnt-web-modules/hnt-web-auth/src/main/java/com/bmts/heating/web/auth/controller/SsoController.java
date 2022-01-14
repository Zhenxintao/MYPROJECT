package com.bmts.heating.web.auth.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.auth0.jwt.interfaces.Claim;
import com.bmts.heating.commons.auth.entity.ClientByCode;
import com.bmts.heating.commons.auth.entity.StatusCode;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.auth.utils.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naming
 * @description
 * @date 2021/8/16 17:12
 **/
@RestController
@Slf4j
public class SsoController {

    @Value("${sso.host}")
    private String host;
    @Value("${sso.tokenPort}")
    private String tokenPort;

    @PostMapping("/getToken")
    @PassToken
    public Response getToken(@RequestBody ClientByCode clientByCode) {
        //1.拿code,clientid,clientsecret 发送get请求center， 返回token
        if (clientByCode.getCode() == null){
            Response.fail("code失效");
        }
        Map<String,String> map = new HashMap<>();
        map.put("code",clientByCode.getCode());
        map.put("client_id",clientByCode.getClient_id());
        map.put("client_secret",clientByCode.getClient_secret());
        Map<String,String> map3 = new HashMap<>();
        //2.token 返回给前端
        //String token = HttpUtils.requestGet("http://10.0.2.60:8056/sso/token",map);
        String token = HttpUtils.requestGet("http://"+host+":"+tokenPort+"/sso/token",map);
        if (token == null){
            return Response.fail("token无效");
        }
        try{
            GsonJsonParser gsonJsonParser = new GsonJsonParser();
            Map<String, Object> map1 = gsonJsonParser.parseMap(token);
            Object o = map1.get("data");
            if (ObjectUtils.isEmpty(o)){
                return Response.fail("token无效");
            }
            Map<String, Object> map2 = gsonJsonParser.parseMap(o.toString());
            Object o1 = map2.get("access_token");
            Object o2 = map2.get("refresh_token");
            if (ObjectUtils.isEmpty(o1)){
                return Response.fail("access_token无效");
            }
            if (ObjectUtils.isEmpty(o2)){
                return Response.fail("refresh_token无效");
            }
            Map<String, Claim> userDate = JwtUtils.verifyToken(o1.toString());
            if (userDate == null){
                return Response.fail();
            }
            map3.put("access_token",o1.toString());
            map3.put("refresh_token",o2.toString());
        }catch (Exception e){
            e.printStackTrace();
            return Response.fail("token解码失败");
        }
        return Response.success(map3);
    }

    @GetMapping("/refresh")
    @PassToken
    public Response refresh(@RequestParam("refresh_token") String refresh_token){
        try{
            Map<String,String> map = new HashMap<>();
            map.put("refresh_token",refresh_token);
            String str = HttpUtils.reloadGet("http://"+host+":"+tokenPort+"/sso/refresh_token", map);
            if (StringUtils.isNotBlank(str)){
                GsonJsonParser gsonJsonParser = new GsonJsonParser();
                Map<String, Object> map1 = null;
                try {
                    map1 = gsonJsonParser.parseMap(str);
                    double codes = (double) map1.get("code");
                    if (StatusCode.CODE_SUCCESS.getCode()==(int)codes){
                        Object o = map1.get("data");
                        String json = JSONUtils.toJSONString(o);
                        Map<String, Object> map2 = gsonJsonParser.parseMap(json);
                        return Response.success(map2);
                    }
                }catch (Exception e){
                    log.error("json 解析错误{}",e);
                }
            }
            return Response.fail(0,"refresh_token过期");
        }catch (Exception e){
            log.error("获取refresh_token错误{}",e);
            return Response.fail(0,"refresh_token过期");
        }
    }

}
