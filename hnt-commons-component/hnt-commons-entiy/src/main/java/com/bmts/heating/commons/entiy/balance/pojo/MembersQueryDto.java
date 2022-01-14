package com.bmts.heating.commons.entiy.balance.pojo;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/2/2 14:23
 **/
@Data
public class MembersQueryDto extends BaseDto {
    int balanceId;
    List<Integer> relevanceIds;
    Integer level;
}
