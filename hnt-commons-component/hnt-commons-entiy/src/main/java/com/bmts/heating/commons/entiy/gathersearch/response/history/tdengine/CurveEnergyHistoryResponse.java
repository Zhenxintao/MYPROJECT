package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import com.bmts.heating.commons.entiy.gathersearch.response.history.PointBasicInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author zxt
 * @description
 * @date 2021/10/21
 **/
@ApiModel(value = "曲线响应实体(byTd)")
@Data
public class CurveEnergyHistoryResponse {
    private List<HistoryEnergyResponse> curves;
    private List<PointBasicInfo> pointBasicInfo;
}
