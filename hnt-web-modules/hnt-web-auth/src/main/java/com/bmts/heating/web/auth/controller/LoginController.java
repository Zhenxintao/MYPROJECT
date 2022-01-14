package com.bmts.heating.web.auth.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import com.bmts.heating.commons.db.service.auth.SysUserService;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.auth.SHA1;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.auth.base.dto.UserInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "登录")
@RequestMapping("login")
@Slf4j
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Value("${user.admin}")
    String userName;
    @Value("${user.pwd}")
    String pwd;
    @Value("${auth.appkey}")
    String appkey;

    @PostMapping
    public Response login(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("登录失败");
        }

        Map<String, String> map = new HashMap<>();
        String token = "";
        if (userInfo.getUsername().equals(userName)) {
            if (userInfo.getPassword().equals(pwd)) {
                SysUser sysUser = new SysUser();
                sysUser.setUsername(userName);
                sysUser.setNickname("超级管理员");
                sysUser.setCenterId(-1);
                token = JwtUtils.createToken(sysUser);
                map.put("role", "admin");
            } else {
                return Response.fail("登录失败");
            }
        } else {
            SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername,
                    userInfo.getUsername()));
            if (user != null && encoder.matches(userInfo.getPassword(), user.getPassword())) {
                if(!user.getStatus())
                    return Response.fail("账号停用状态，无法登陆");
                token = JwtUtils.createToken(user);
                map.put("name", user.getNickname());
            } else return Response.fail("登陆失败");

        }
        if (StringUtils.isBlank(token))
            return Response.fail("登陆失败");
        map.put("token", token);
        return Response.success(map);
    }


    @PostMapping("logout")
    public Response logout() {
        Response response = new Response();
        response.setData("success");
        return response;
    }

    private boolean validate(HttpServletRequest request) {
        try {
            String nonce = request.getHeader("nonce");
            String nonce1 = request.getHeader("nonce1");
            String appKey = appkey;
            String timestamp = request.getHeader("timestamp");
            if (StringUtils.isBlank(nonce) || StringUtils.isBlank(nonce1) || StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp))
                return false;
            long time = Long.valueOf(timestamp);
            String checkSumHeader = request.getHeader("checkSum");
            if (SHA1.encode(nonce.concat(nonce1).concat(appKey).concat(timestamp)).toLowerCase().equals(checkSumHeader))
                return true;
            return false;
        } catch (Exception e) {
            log.error("validate header cause exception {}", e);
            return false;
        }

    }

}
