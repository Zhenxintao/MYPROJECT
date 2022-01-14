package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 能耗历史数据--返回数据响应实体
 */
@Data
public class HistoryEnergyDataResponse {
    /**
     * 能耗历史数据集
     */
    private List<HistoryEnergyResponse> responseData;
    /**
     * 查询数据总条数
     */
    private Integer total;
}
