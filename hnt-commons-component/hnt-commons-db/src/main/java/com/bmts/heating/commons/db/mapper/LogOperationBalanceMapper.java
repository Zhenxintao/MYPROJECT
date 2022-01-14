package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationControl;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pxf
 * @since 2020-11-16
 */
public interface LogOperationBalanceMapper extends BaseMapper<LogOperationBalance> {
    @Insert("INSERT INTO log_operation_balance (userName,createTime,ipAddress,module,button,description) VALUES (#{userName},#{createTime},#{ipAddress},#{module},#{button},CONCAT(#{descriptionHead},(SELECT `name` from balanceNet WHERE Id = #{id}),#{descriptionTrail})) ")
    int insertLogById(BalanceLogDto dto);

    @Insert(" INSERT INTO log_operation_balance (userName,createTime,ipAddress,module,button,description) SELECT #{userName},#{createTime},#{ipAddress},#{module},#{button},CONCAT(#{descriptionHead},t.netBalanceName,t.stationName,'-',t.cabinetName,'-',t.systemName,#{descriptionTrail}) as description FROM (" +
            "SELECT n.`name` as netBalanceName,hs.`name` as systemName,hc.`name` as cabinetName,ht.`name` as stationName FROM balanceMembers b LEFT JOIN balanceNet n on b.balanceNetId = n.id LEFT JOIN heatSystem hs on b.relevanceId = hs.id LEFT JOIN heatCabinet hc on hs.heatCabinetId=hc.id LEFT JOIN heatTransferStation ht on hc.heatTransferStationId=ht.id WHERE FIND_IN_SET(b.id,#{ids}) ) t ")
    int insertLogByIds(BalanceLogDto dto);
}
