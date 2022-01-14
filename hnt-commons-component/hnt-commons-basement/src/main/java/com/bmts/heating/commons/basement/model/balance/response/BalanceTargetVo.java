package com.bmts.heating.commons.basement.model.balance.response;

import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/2/6 13:09
 **/
@Data
@ApiModel("对应菜单项 历史记录")
public class BalanceTargetVo extends BalanceTargetHistory {
    @ApiModelProperty("全网平衡名称")
    private String balanceNetName;
}
