package com.bmts.heating.commons.jwt.interceptor;

import com.bmts.heating.commons.jwt.dto.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(TokenException.class)
    public Response handleException(Exception exception) {
        return Response.builder().code(401).build();
    }
}
