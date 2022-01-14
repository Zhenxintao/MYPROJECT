package com.bmts.heating.bussiness.cache.joggle;

import com.bmts.heating.commons.basement.model.cache.PointRank;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorBeyondDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.RankDto;
import com.bmts.heating.commons.entiy.gathersearch.request.RankSection;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/10 17:17
 **/
@Api(tags = "排行查询")
@RestController
@RequestMapping("rank")
public class RankJoggle {
    @Autowired
    RedisCacheService redisCacheService;

    @ApiOperation("查询实时区间排行")
    @PostMapping
    public List<PointRank> queryRank(@RequestBody RankDto rankDto) throws MicroException {
        List<PointRank> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rankDto.getPoints())) {
            for (RankSection point : rankDto.getPoints()) {
                if (!StringUtils.isNotBlank(point.getPointName()))
                    break;
                PointRank pointRank = redisCacheService.queryRank(point.getPointName(), point.getStart(), point.getEnd(), rankDto.getCount(), rankDto.isAsc(), TreeLevel.HeatSystem.level());
                pointRank.setColumnName(point.getPointName());
                result.add(pointRank);
            }
        }
        return result;
    }

    @GetMapping("/{pointName}/{isAsc}")
    public PointRank queryRankByPoint(@PathVariable String pointName, @PathVariable boolean isAsc) throws MicroException {
        PointRank pointRank = redisCacheService.queryRank(pointName, -10000000000000.000, 10000000000000.000, 10000000, isAsc, TreeLevel.HeatSystem.level());
        return pointRank;
    }

    @PostMapping("/beyond")
    public PointRank queryRankValue(@RequestBody MonitorBeyondDto dto) throws MicroException {
        String columnName = dto.getColumnName();
        double value = dto.getValue();
        // 查询类型：-2 小于，-1小于等于，0 等于，1大于等于，2大于
        switch (dto.getType()) {
            case -2:
                return redisCacheService.queryRank(columnName, -10000000000000.000, (value - 0.01), 10000000, false, TreeLevel.HeatSystem.level());
            case -1:
                return redisCacheService.queryRank(columnName, -10000000000000.000, value, 10000000, false, TreeLevel.HeatSystem.level());
            case 0:
                return redisCacheService.queryRank(columnName, value, value, 10000000, false, TreeLevel.HeatSystem.level());
            case 1:
                return redisCacheService.queryRank(columnName, value, 10000000000000.000, 10000000, true, TreeLevel.HeatSystem.level());
            case 2:
                return redisCacheService.queryRank(columnName, (value + 0.01), 10000000000000.000, 10000000, true, TreeLevel.HeatSystem.level());
        }
        return null;
    }
}
