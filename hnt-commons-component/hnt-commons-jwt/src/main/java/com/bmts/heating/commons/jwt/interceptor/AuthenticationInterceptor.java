package com.bmts.heating.commons.jwt.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.jwt.config.IgnoreConfig;
import com.bmts.heating.commons.jwt.dto.Response;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    IgnoreConfig ignoreConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        String path = request.getRequestURL().toString();
        if (path.contains("swagger") || path.contains("webjars") || path.contains("v2") || path.contains("login"))
            return true;
        else if (ignoreConfig.getUrls().length > 0 && Arrays.stream(ignoreConfig.getUrls()).anyMatch(x -> path.contains(x)))
            return true;
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        response.setCharacterEncoding("UTF-8");
        //获取 header里的token
        final String token = request.getHeader("authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        else {
            if (token == null) {
                throw new TokenException();
            }
            Map<String, Claim> userData = JwtUtils.verifyToken(token);
            if (userData == null) {
                throw new TokenException();
            }
            Integer id = userData.get("user_id").asInt();
            //拦截器 拿到用户信息，放到request中
            request.setAttribute("user_id", id);//center_id
            request.setAttribute("user_name", userData.get("user_name").asString());
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
