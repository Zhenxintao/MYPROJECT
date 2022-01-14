package com.bmts.heating.commons.entiy.baseInfo.response;

import com.bmts.heating.commons.entiy.baseInfo.enums.NetFlagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DefaultRealHeaders
 * @Description: 默认表头
 * @Author: pxf
 * @Date: 2020/12/11 15:55
 * @Version: 1.0
 */

@Data
@ApiModel("表头")
public class DefaultRealHeadersDto {

    @ApiModelProperty("标准点Id")
    private Integer pointStandardId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("1.参数汇总 2.全网平衡  3.单站监测历史  4.生产调度历史")
    private Integer type;

    @ApiModelProperty("参量名称")
    private String name;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;

    @ApiModelProperty("参量分类名称")
    private String pointName;

    @ApiModelProperty("单位类型")
    private String unit;

    @ApiModelProperty("具体单位")
    private String unitDisable;

    @ApiModelProperty("网测类型 0.公用 1.一次侧 2.二次侧")
    private String netFlagName;

    public void setNetFlag(Integer netFlag) {
        if (netFlag != null) {
            this.netFlagName = NetFlagType.getValue(netFlag);
        }
        this.netFlag = netFlag;
    }
}
