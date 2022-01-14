package com.bmts.heating.commons.basement.model.db.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bmts.heating.commons.basement.config.ExcelColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@ApiModel("标准点表导入类")
public class PointStandardImportResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参量名称
     */
    @ApiModelProperty("参量名称")
    @ExcelColumn(value = "参量名称", col = 1)
    private String name;


    @ApiModelProperty("1.AI模拟量 2.DI 数字量 3.TX 日期时间 名称")
    @ExcelColumn(value = "类型", col = 2)
    private String pointTypeName;


    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    @ExcelColumn(value = "标签名称", col = 3)
    @TableField("columnName")
    private String columnName;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    @ExcelColumn(value = "单位", col = 4)
    private String unit;

    /**
     * 数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    @ExcelColumn(value = "数据类型", col = 5)
    private String dataTypeName;

    /**
     * 网测类型 0公用 1.一次测 2.二次测
     */
    @ApiModelProperty("网侧类型 0.公用 1.一次侧 2.二次侧")
    @ExcelColumn(value = "网侧类型", col = 6)
    private String netFlagName;

    /**
     * 是否参与运算
     */
    @ApiModelProperty("是否参与运算 是  否 ")
    @ExcelColumn(value = "是否参与运算", col = 7)
    private String computeMsg;


    @ApiModelProperty("fix值类型名称")
    @ExcelColumn(value = "fix值类型", col = 8)
    private String fixValueTypeName;


    @ApiModelProperty("参量分类名称")
    @ExcelColumn(value = "参量分类名称", col = 9)
    private String pointName;

    @ApiModelProperty("所属类型")
    @ExcelColumn(value = "所属类型", col = 10)
    private String levelName;


}
