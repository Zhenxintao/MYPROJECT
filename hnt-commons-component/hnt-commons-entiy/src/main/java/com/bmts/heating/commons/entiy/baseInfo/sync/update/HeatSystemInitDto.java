package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:41
 **/
@ApiModel("系统同步实体")
@Data
public class HeatSystemInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private BigInteger num;
    /**
     * 同步父级编号
     */
    @ApiModelProperty(value = "同步父级编号", required = true)
    private Integer parentNum;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 机组编号  0号机组代表控制柜或总管
     */
    @ApiModelProperty(value = "机组编号： 0 表示机组代表控制柜或总管 ", required = true)
    private Integer number;

    /**
     * 热表类型
     */
    @ApiModelProperty("热表类型：1为采暖 2为生活水")
    private Integer hotListType;

//    /**
//     * 热表口径
//     */
//    @ApiModelProperty("热表口径")
//    private Integer hotListCaliber;

//    /**
//     * 热表编号
//     */
//    @ApiModelProperty("热表编号")
//    private Integer hotListNumber;

//    /**
//     * 官网口径
//     */
//    @ApiModelProperty("官网口径")
//    private Integer webSitCaliber;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;


//    /**
//     * 唯一编码
//     */
//    @ApiModelProperty("唯一编码")
//    private String code;


    /**
     * 散热类型：1.挂片采暖 2.地板辐射 3.热风幕 4.风机盘管 5.其他
     */
    @ApiModelProperty(value = "散热类型：1.挂片采暖 2.地板辐射 3.热风幕 4.风机盘管 5.其他", required = true)
    private Integer heatingType;

    /**
     * 换热类型：1.间联 2.混水 3.直供 4.汽水换热 5.大温差机组
     */
    @ApiModelProperty(value = "换热类型：1.间联 2.混水 3.直供 4.汽水换热 5.大温差机组", required = true)
    private Integer heatTransferType;

    /**
     * 节能类型：1.非节能建筑 2.一步节能建筑30% 3.二步节能建筑50% 4.三步节能建筑65% 5.四步节能建筑75% 6.其他
     */
    @ApiModelProperty("节能类型：1.非节能建筑 2.一步节能建筑30% 3.二步节能建筑50% 4.三步节能建筑65% 5.四步节能建筑75% 6.其他")
    private Integer energySavingType;

//    /**
//     * 建筑类型：1.民宅 2.学校 3.商业建筑 4.工业建筑 5.保障型建筑(幼儿园、养老院、政府小区等)
//     */
//    @ApiModelProperty("建筑类型：1.民宅 2.学校 3.商业建筑 4.工业建筑 5.保障型建筑(幼儿园、养老院、政府小区等)")
//    private Integer buildingType;

}
