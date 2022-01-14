package com.bmts.heating.commons.entiy.gathersearch.response.issue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("一次调节阀响应实体类")
public class CVModeResponse {

    @ApiModelProperty(" 0:定值  1:二次供温恒定  4:恒流")
    private Integer options;

    @ApiModelProperty("一次阀门给定开度")
    private Double cvSV;
    @ApiModelProperty("一次阀门实际开度")
    private Double cvU;

    @ApiModelProperty("二次供温恒定")
    private CVTg cvTg;

    @Data
    public static class CVTg {
        @ApiModelProperty("二次供温设定值")
        private Double cvTgSP;

        @ApiModelProperty("实时供温")
        private Double t2g;

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


    @ApiModelProperty("恒定流量设定值")
    private Double cvFSP;

    @ApiModelProperty("一次调节阀设置步进")
    private Double cvStep;
    @ApiModelProperty("一次调节阀最小开度")
    private Double cvMin;
    @ApiModelProperty("一次调节阀最大开度")
    private Double cvMax;
}
