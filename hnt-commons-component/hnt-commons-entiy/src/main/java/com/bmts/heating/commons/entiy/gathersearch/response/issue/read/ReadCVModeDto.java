package com.bmts.heating.commons.entiy.gathersearch.response.issue.read;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("一次调节阀读取实体类")
public class ReadCVModeDto {

    @ApiModelProperty(" 0:定值  1:二次供温恒定  4:恒流")
    private Integer options;

    @ApiModelProperty("开度设定值")
    private Double cvMsp;

    @ApiModelProperty("二次供温恒定")
    private CVTg cvTg;

    @Data
    public class CVTg {
        @ApiModelProperty("二次供温设定值")
        private Double cvTgSP;
        @ApiModelProperty("一次调节阀设置步进")
        private Double cvStep;
        @ApiModelProperty("一次调节阀最小开度")
        private Double cvMin;
        @ApiModelProperty("一次调节阀最大开度")
        private Double cvMax;
        @ApiModelProperty("0时段偏差设定值")
        private Double tgSPTime0;
        @ApiModelProperty("2时段偏差设定值")
        private Double tgSPTime1;
        @ApiModelProperty("4时段偏差设定值")
        private Double tgSPTime2;
        @ApiModelProperty("6时段偏差设定值")
        private Double tgSPTime3;
        @ApiModelProperty("8时段偏差设定值")
        private Double tgSPTime4;
        @ApiModelProperty("10时段偏差设定值")
        private Double tgSPTime5;
        @ApiModelProperty("12时段偏差设定值")
        private Double tgSPTime6;
        @ApiModelProperty("14时段偏差设定值")
        private Double tgSPTime7;
        @ApiModelProperty("16时段偏差设定值")
        private Double tgSPTime8;
        @ApiModelProperty("18时段偏差设定值")
        private Double tgSPTime9;
        @ApiModelProperty("20时段偏差设定值")
        private Double tgSPTime10;
        @ApiModelProperty("22时段偏差设定值")
        private Double tgSPTime11;
    }

    @ApiModelProperty("恒流")
    private CVFs cvFs;

    @Data
    public class CVFs {
        @ApiModelProperty("恒定流量设定值")
        private Double cvFSP;
        @ApiModelProperty("一次调节阀设置步进")
        private Double cvStep;
        @ApiModelProperty("一次调节阀最小开度")
        private Double cvMin;
        @ApiModelProperty("一次调节阀最大开度")
        private Double cvMax;
    }

}
