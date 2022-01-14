package com.bmts.heating.commons.entiy.baseInfo.sync.add;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class SyncHeatDicDto {
    /**
     * 字典标识编码
     */
    @ApiModelProperty("字典标识编码")
    private String code;
    @ApiModelProperty("具体信息项")
    List<SyncHeatDicDetailDto> data;
    @ApiModelProperty("字典名称")
    private String  message;
}
