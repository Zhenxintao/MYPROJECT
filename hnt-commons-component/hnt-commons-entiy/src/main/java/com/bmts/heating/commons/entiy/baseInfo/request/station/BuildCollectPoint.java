package com.bmts.heating.commons.entiy.baseInfo.request.station;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName BuildCollectPoint
 * @Author naming
 * @Date 2020/11/21 16:28
 **/
@Data
@ApiModel("采集量参数")
public class BuildCollectPoint extends PointCollectConfig {
   private String properties;
}
