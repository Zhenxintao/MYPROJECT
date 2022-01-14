package com.bmts.heating.commons.jwt.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("响应类")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private int code;
    private String msg;
    private Object data;

}
