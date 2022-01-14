package com.bmts.heating.commons.entiy.report.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ReportFormResponse
 * @Description: 报表返回类
 * @Author: pxf
 * @Date: 2021/9/10 13:44
 * @Version: 1.0
 */
@Data
@ApiModel("报表响应类")
public class ReportFormResponse implements Serializable {

    @ApiModelProperty("报表响应数据")
    private Object data;

    public ReportFormResponse() {

    }

    public ReportFormResponse(Object data) {
        this.data = data;
    }


    public static ReportFormResponse data(Object data) {
        return new ReportFormResponse(data);
    }
}
