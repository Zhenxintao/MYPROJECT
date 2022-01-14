package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报警级别 json")
public class AlarmRankJson {

    @ApiModelProperty("事故高报警级别")
    private Integer accidentHighRank;

    @ApiModelProperty("运行高报警级别")
    private Integer runHighRank;

    @ApiModelProperty("运行低报警级别")
    private Integer runLowerRank;

    @ApiModelProperty("事故低报警级别")
    private Integer accidentLowerRank;


    @ApiModelProperty("事故高报警描述")
    private String accidentHighDesc;

    @ApiModelProperty("运行高报警描述")
    private String runHighDesc;

    @ApiModelProperty("运行低报警描述")
    private String runLowerDesc;

    @ApiModelProperty("事故低报警描述")
    private String accidentLowerDesc;


}
