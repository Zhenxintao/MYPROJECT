package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationInfo;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceMemberResponse;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceSystemInfo;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceMembersBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface BalanceMembersMapper extends BaseMapper<BalanceMembers> {
    @Select("SELECT b.balanceNetId,b.relevanceId,b.`level` as 'level',h.heatArea,b.controlType,b.compensation,b.`status` as 'status',h.`name` as 'systemName',h.heatingType,c.compensationValue,p.pointName FROM balanceMembers b "+
            "LEFT JOIN heatSystem h ON b.relevanceId = h.id " +
            "LEFT JOIN balanceCompensation c ON b.balanceCompensationid = c.id " +
            "LEFT JOIN pointPatternConfig p ON b.controlType = p.controlType  ${ew.customSqlSegment}")
    List<BalanceSystemInfo> balanceSystemInfo(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT bm.*,bc.name,bc.compensationValue FROM balanceMembers bm LEFT JOIN balanceCompensation bc ON bm.balanceCompensationId=bc.id ${ew.customSqlSegment}")
    IPage<BalanceMembersBase> queryBase(Page<BalanceMembersBase> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT b.*,s.heatTransferStationName,s.heatSystemName,s.heatSystemArea FROM balanceMembers b LEFT JOIN stationFirstNetBaseView s on b.relevanceId=s.heatSystemId ${ew.customSqlSegment}")
    List<BalanceMemberResponse> queryBalanceMemberResponse(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
