package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("异常数据查询类")
@Data
public class AbnormalDto {
    @ApiModelProperty("当前页")
    private int currentPage = 1;
    @ApiModelProperty("页大小")
    private int pageCount = 20;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("系统id 集合")
    private List<Integer> groupId;
    @ApiModelProperty("所属： 1.站 2.源 对应枚举 AbnormalLevel")
    private Integer level;

    @ApiModelProperty("站名")
    private String stationName;

    /**
     * 排序方式，ASC/DESC"
     */
    @ApiModelProperty("排序方式，ASC/DESC ")
    private String order;
    @ApiModelProperty("异常数据类型：3、设备损坏  4、数据异常 ")
    private Integer abnormalType;

}
