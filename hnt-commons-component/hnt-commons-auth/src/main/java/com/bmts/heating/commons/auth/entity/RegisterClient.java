package com.bmts.heating.commons.auth.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterClient {
    @NotBlank(message = "必填项")
    private String client_id;
    @NotBlank(message = "必填项")
    private String client_secret;
    @ApiModelProperty(hidden = true)
    private String client_ip;
}
