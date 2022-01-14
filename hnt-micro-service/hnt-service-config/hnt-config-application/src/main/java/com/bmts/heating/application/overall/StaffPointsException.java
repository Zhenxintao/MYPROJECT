package com.bmts.heating.application.overall;

import com.bmts.heating.commons.utils.restful.ResponseCode;
import lombok.Data;

/**
 * 全局异常类
 */
@Data
public class StaffPointsException extends RuntimeException{
    private int code;

    private String msg;

    public StaffPointsException (Exception e) {
        super(e);
    }

    public StaffPointsException (int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public StaffPointsException (ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }
}
