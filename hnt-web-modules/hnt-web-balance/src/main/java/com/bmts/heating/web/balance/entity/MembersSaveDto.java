package com.bmts.heating.web.balance.entity;

import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/2/2 15:03
 **/
@Data
@ApiModel("全网平衡批量保存系统")
public class MembersSaveDto {
    @ApiModelProperty("全网平衡id")
    int balanceId;
    @ApiModelProperty("全网平衡组员")
    List<BalanceMembers> members;
}
