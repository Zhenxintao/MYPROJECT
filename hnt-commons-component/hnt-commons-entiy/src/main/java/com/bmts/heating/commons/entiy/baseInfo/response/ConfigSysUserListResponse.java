package com.bmts.heating.commons.entiy.baseInfo.response;

import com.bmts.heating.commons.entiy.baseInfo.enums.NetFlagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("自定义列表响应类")
@Data
public class ConfigSysUserListResponse {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("自定义列表名称")
    private String name;

    @ApiModelProperty("是否默认")
    private Boolean isDefault;


    // 组织机构集合
    @ApiModelProperty("组织机构Id 集合")
    private List<Integer> heatOrganizationIds;

    // 分类查询
    @ApiModelProperty("分类查询")
    private String typeJson;

    // 站点集合
    @ApiModelProperty("站点集合")
    private List<HeatStation> staionIds;

    @Data
    public static class HeatStation {
        @ApiModelProperty("站点id")
        private Integer id;
        @ApiModelProperty("站名称")
        private String name;
        @ApiModelProperty("公司组织机构id")
        private Integer pid;
        @ApiModelProperty("代表站")
        private String properties;
    }

    // 参量
    @ApiModelProperty("参量标准点表")
    private List<PointStandard> pointStandardIds;

    @Data
    public static class PointStandard {

        @ApiModelProperty("点表id")
        private Integer id;
        @ApiModelProperty("参量名称")
        private String name;
        @ApiModelProperty("网测类型 0.公用 1.一次侧 2.二次侧")
        private Integer netFlag;
        @ApiModelProperty("标签名称")
        private String columnName;

        @ApiModelProperty("参量分类名称")
        private String pointName;

        @ApiModelProperty("网测类型 0.公用 1.一次侧 2.二次侧")
        private String netFlagName;

        @ApiModelProperty("单位类型")
        private String unit;

        @ApiModelProperty("具体单位")
        private String unitDisable;

        public void setNetFlag(Integer netFlag) {
            if (netFlag != null) {
                this.netFlagName = NetFlagType.getValue(netFlag);
            }
            this.netFlag = netFlag;
        }
    }


}
