package com.bmts.heating.web.auth.base.handller;


import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response validExceptionHandler(MethodArgumentNotValidException e) {
        Response result = new Response();
        StringBuilder sb = new StringBuilder();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (ObjectError error : errors)
            sb.append(error.getDefaultMessage());
        return Response.fail(sb.toString());
    }
}