package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author naming
 * @description 曲线入参
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("曲线入参")
public class CurveDto {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("1.分钟 2.小时 3.天 4.能耗小时  后续根据需要扩充")
    private int queryType;
    @ApiModelProperty("单位类型")
    private List<String> listUnitType;
    @ApiModelProperty("系统集合")
    private Integer[] listSystem;
    @ApiModelProperty("1：一次网数据， 2：二次网数据，3：室温数据,4:能耗数据")
    private int sourceType;//一次、二次、室温，能耗
    @ApiModelProperty("点标签名集合")
    private String[] listPointName;
}
