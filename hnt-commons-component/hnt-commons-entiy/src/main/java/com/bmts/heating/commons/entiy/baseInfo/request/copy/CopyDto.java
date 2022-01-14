package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CopyDto
 * @Author naming
 * @Date 2020/11/20 9:42
 **/
@Data
@ApiModel(value = "复制站参数实体")
public class CopyDto extends BaseUser {

    @ApiModelProperty(value = "复制到目标Id集合")
    private List<Integer> targetIds;
    @ApiModelProperty(value = "复制源信息")
    private List<CopySourceDto> copySourceDtos;
    @ApiModelProperty(value = "参量方式 0.全部 1.采集量 2.控制量")
    private Integer type;

    @ApiModelProperty("复制源id、即热力站站id 或热源id")
    private Integer copySourceId;

}
