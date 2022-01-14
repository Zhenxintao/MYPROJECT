package com.bmts.heating.commons.db.mapper;



import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.balance.response.BalanceControlLog;
import com.bmts.heating.commons.basement.model.balance.response.BalanceRejectVo;
import com.bmts.heating.commons.basement.model.balance.response.BalanceTargetVo;
import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface BalanceTargetHistoryMapper extends BaseMapper<BalanceTargetHistory> {

	@Select("SELECT brh.*,bn.name balanceNetName FROM balanceRejectHistory brh " +
			"INNER JOIN balanceNet bn ON brh.balanceNetId = bn.id ${ew.customSqlSegment}")
	IPage<BalanceRejectVo> pageUnionReject(Page<BalanceTargetVo> page, @Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("SELECT bnc.*,bn.name balanceNetName FROM balanceNetControlLog bnc " +
			"INNER JOIN balanceNet bn ON bnc.balanceNetId = bn.id ${ew.customSqlSegment}")
	IPage<BalanceControlLog> pageUnionControl(Page<BalanceTargetVo> page, @Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("SELECT bth.*,bn.name balanceNetName FROM balanceTargetHistory bth " +
			"INNER JOIN balanceNet bn ON bth.balanceNetId = bn.id ${ew.customSqlSegment}")
	IPage<BalanceTargetVo> pageUnionTarget(Page<BalanceTargetVo> page, @Param(Constants.WRAPPER) Wrapper wrapper);


}
