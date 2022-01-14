package com.bmts.heating.web.auth.base.handller;


import com.bmts.heating.commons.auth.entity.StatusCode;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception exception) {
        exception.printStackTrace();
        return Response.fail();
    }
}
