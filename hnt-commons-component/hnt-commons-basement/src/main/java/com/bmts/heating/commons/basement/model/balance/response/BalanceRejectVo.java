package com.bmts.heating.commons.basement.model.balance.response;

import com.bmts.heating.commons.basement.model.db.entity.BalanceRejectHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/2/6 13:12
 **/
@Data
@ApiModel("对应前端 菜单项报警记录")
public class BalanceRejectVo extends BalanceRejectHistory {
    @ApiModelProperty("换热站名称")
    private String heatTransferStationName;
    @ApiModelProperty("控制柜名称")
    private String heatCabinetName;
    @ApiModelProperty("系统名称")
    private String heatSystemName;
    @ApiModelProperty("虚拟网名称")
    private String balanceNetName;
}
