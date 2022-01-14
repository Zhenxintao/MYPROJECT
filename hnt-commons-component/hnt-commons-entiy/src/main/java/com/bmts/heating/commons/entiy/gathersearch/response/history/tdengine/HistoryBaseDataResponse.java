package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 基础历史数据--返回数据响应实体
 */
@Data
public class HistoryBaseDataResponse {
    /**
     * 历史数据集
     */
    private List<HistoryBaseResponse> responseData;
    /**
     * 查询数据总条数
     */
    private Integer total;
}
