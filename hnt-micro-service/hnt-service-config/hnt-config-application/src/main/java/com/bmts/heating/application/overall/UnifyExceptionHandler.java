package com.bmts.heating.application.overall;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局Restful请求异常处理
 */
@RestControllerAdvice
@Component
public class UnifyExceptionHandler {
    private Logger LOGGER = LoggerFactory.getLogger(UnifyExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Response handlerCommonException (Exception e) {
        Response response = new Response();
        response.setCode(ResponseCode.FAIL.getCode());
        response.setMsg(ResponseCode.FAIL.getMsg());
        LOGGER.info("UnifyExceptionHandler.handlerCommonException exception:" + e);
        return response;
    }
    // 报StaffPointException时，对其进行拦截并处理的方法
    @ExceptionHandler( StaffPointsException.class)
    public Response handlerCustomizeException (StaffPointsException e) {
        Response responseDto = new Response();
        responseDto.setCode(e.getCode());
        responseDto.setMsg(e.getMessage());
        LOGGER.info("UnifyExceptionHandler.handlerCustomizeException StaffPointsException:" + e);
        return responseDto;
    }
}
