package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/8 9:58
 **/
@ApiModel(value = "曲线响应实体")
@Data
public class CurveResponse {
    private List<Curve> curves;
    private List<PointBasicInfo> pointBasicInfo;
}
