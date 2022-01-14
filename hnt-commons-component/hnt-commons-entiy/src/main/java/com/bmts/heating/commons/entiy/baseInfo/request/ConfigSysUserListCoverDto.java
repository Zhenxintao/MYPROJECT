package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("自定义列表覆盖类")
@Data
public class ConfigSysUserListCoverDto {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("用户id")
    private int userId;


    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    // 站点集合
    @ApiModelProperty("站点集合")
    private List<Integer> staionIds;

    // 组织机构集合
    @ApiModelProperty("组织机构集合")
    private List<Integer> heatOrganizationIds;

    // 参量
    @ApiModelProperty("参量标准点表Id")
    private List<Integer> pointStandardIds;

    // 分类查询
    @ApiModelProperty("分类查询")
    private String typeJson;


//    @Data
//    public class TypeJson {
//
//        //建筑类型
//        @ApiModelProperty("建筑类型")
//        private List<Integer> buildTypes;
//        //保温结构
//        @ApiModelProperty("保温结构")
//        private List<Integer> insulationConstructions;
//        //支付方式
//        @ApiModelProperty("支付方式")
//        private List<Integer> payTypes;
//        //供热方式
//        @ApiModelProperty("供热方式")
//        private List<Integer> heatTypes;
//        //管理方式
//        @ApiModelProperty("管理方式")
//        private List<Integer> manageTypes;
//        //站点类型
//        @ApiModelProperty("站点类型")
//        private List<Integer> StationTypes;
//        // 热源集合
//        @ApiModelProperty("热源Id")
//        private List<Integer> heatSourceIds;
//
//    }


}
