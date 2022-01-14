package com.bmts.heating.commons.basement.model.cache;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author: naming
 * @Description:
 * @Date: Create in 2020/9/28 11:47
 * @Modified by
 */
@ApiModel("查询实体")
@Data
public class SearchBase {
    private int pageCurrent = 1;
    private int pageSize = 20;
    private String sortName;
    private String sortType;
}
