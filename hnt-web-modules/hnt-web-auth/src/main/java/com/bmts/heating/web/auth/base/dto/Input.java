package com.bmts.heating.web.auth.base.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("分页参数")
@Data
public class Input {
    private Integer currentPage=1;
    private Integer pagesize=10;
}
