package com.bmts.heating.bussiness.netBalance.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNet;
import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.BalanceMembersMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.balance.pojo.*;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceMembersBase;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.BalanceCurveDto;
import com.bmts.heating.commons.entiy.gathersearch.request.CurveDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.Curve;
import com.bmts.heating.commons.entiy.gathersearch.response.history.CurveResponse;
import com.bmts.heating.commons.utils.cron.CronUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.execute.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/1/29 11:19
 **/
@RestController
@RequestMapping("netbalance")
@Slf4j
public class NetBalanceJoggle {
    @Autowired
    BalanceMembersService balanceMembersService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    BalanceTargetHistoryService balanceTargetHistoryService;
    @Autowired
    BalanceMembersMapper balanceMembersMapper;
    @Autowired
    private RedisCacheService redisCacheService;

    @GetMapping("/members/{id}/{level}")
    public List<BalanceMembers> loadMembersByBalanceId(@PathVariable int id, @PathVariable int level) {
        try {
            QueryWrapper<BalanceMembers> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(BalanceMembers::getBalanceNetId, id).eq(BalanceMembers::getLevel, level);
            return balanceMembersService.list(queryWrapper);
        } catch (Exception e) {
            log.error("query balance members cause exception {}", e);
            return null;
        }
    }

    @Autowired
    BalanceNetService balanceNetService;

    @PostMapping("/members/list")
    public Response loadMembers(@RequestBody MembersQueryDto membersDto) {
        try {
            QueryWrapper<BalanceMembersBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bm.balanceNetId", membersDto.getBalanceId());
            if (membersDto.getLevel() != null) {
                queryWrapper.eq("bm.level", membersDto.getLevel());
            }
            if (!CollectionUtils.isEmpty(membersDto.getRelevanceIds())) {
                queryWrapper.in("bm.RelevanceId", membersDto.getRelevanceIds());
            }
            Page<BalanceMembersBase> page = new Page<>(membersDto.getCurrentPage(), membersDto.getPageCount());

            IPage<BalanceMembersBase> pages = balanceMembersMapper.queryBase(page, queryWrapper);
            List<StationFirstNetBaseView> firstNetBases = stationFirstNetBaseViewService.list();
            List<BalanceMembersBase> records = pages.getRecords();
            List<MembersVo> members = new ArrayList<>(records.size());
            for (BalanceMembersBase record : records) {
                MembersVo vo = new MembersVo();
                StationFirstNetBaseView firstNetBase = firstNetBases.stream().filter(x -> x.getHeatSystemId().equals(record.getRelevanceId())).findFirst().orElse(null);
                if (firstNetBase != null) {
                    vo.setHeatSystemArea(firstNetBase.getHeatSystemArea());
                    vo.setHeatTransferStationName(firstNetBase.getHeatTransferStationName());
                    vo.setHeatSystemName(firstNetBase.getHeatSystemName());
                    vo.setHeatCabinetName(firstNetBase.getHeatCabinetName());
                }
                vo.setControlType(record.getControlType());
                vo.setBalanceNetId(record.getBalanceNetId());
                vo.setLevel(record.getLevel());
                vo.setId(record.getId());
                vo.setStatus(record.getStatus());
                vo.setBalanceCompensationId(record.getBalanceCompensationId());
                vo.setCompensationValue(record.getCompensationValue());
                vo.setName(record.getName());
                members.add(vo);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("current", pages.getCurrent());
            result.put("size", pages.getSize());
            result.put("total", pages.getTotal());
            result.put("pages", pages.getPages());
            result.put("records", members);
            return Response.success(result);
        } catch (Exception e) {
            log.error("query balance members cause exception {}", e);
            return Response.fail();
        }
    }

    @DeleteMapping("/members/delete")
    public Response removeMembers(@RequestBody List<Integer> ids) {
        try {
            boolean result = balanceMembersService.removeByIds(ids);
            if (!result) {
                return Response.fail("操作失败");
            }
        } catch (Exception e) {
            log.error("delete balance members cause execption--{}", e);
            return Response.fail();
        }
        return Response.success();
    }

    @PostMapping("/members/save")
    public Response saveMemebers(@RequestBody List<BalanceMembers> balanceMembers) {
        try {
            List<BalanceMembers> list = balanceMembersService
                    .list(Wrappers.<BalanceMembers>lambdaQuery().eq(BalanceMembers::getBalanceNetId, null));
            List<BalanceMembers> updateMembers = new ArrayList<>();
            for (BalanceMembers member : list) {
                BalanceMembers containItem = balanceMembers.stream().filter(x ->
                        x.getLevel().equals(member.getLevel()) && x.getRelevanceId().equals(member.getRelevanceId())
                ).findFirst().orElse(null);
                balanceMembers.remove(containItem);
                assert containItem != null;
                member.setBalanceNetId(containItem.getBalanceNetId());
                member.setId(containItem.getId());
                updateMembers.add(member);
            }
            if (updateMembers.size() > 0) {
                balanceMembersService.updateBatchById(updateMembers);
            }
            if (balanceMembers.size() > 0) {
                boolean result = balanceMembersService.saveBatch(balanceMembers);
                if (!result) {
                    return Response.fail("操作失败");
                }
            }

        } catch (Exception e) {
            log.error("save balance members cause execption {}", e);
            return Response.fail();
        }
        return Response.success();
    }

    @PutMapping("/members/update")
    public Response update(@RequestBody BalanceMembers entity) {
        try {
            boolean result = balanceMembersService.updateById(entity);
            if (!result) {
                return Response.fail("操作失败");
            }
        } catch (Exception e) {
            log.error("update balance members cause execption {}", e);
            return Response.fail();
        }
        return Response.success();
    }

    @GetMapping("/memebers/contains")
    public List<BalanceMembers> queryAll() {
        return balanceMembersService.list(Wrappers.<BalanceMembers>lambdaQuery().gt(BalanceMembers::getBalanceNetId, 0));

    }


    @ApiOperation("查询全网平衡信息")
    @GetMapping("/selNetBalanceInfo/{id}")
    public Response selNetBalanceInfo(@PathVariable int id) {
        try {
            QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
            queryWrapper.func(s -> {
                if (id != 0) {
                    queryWrapper.eq("Id", id);
                }
            });
            List<BalanceNet> balanceNetList = balanceNetService.list(queryWrapper);
            List<BalanceBasicVo> balanceBasicVosList = balanceNetList.stream().map(obj -> {
                BalanceBasicVo balanceBasicVo = new BalanceBasicVo();
                balanceBasicVo.setId(obj.getId());
//                balanceBasicVo.setNextTime(CronUtil.nextNodeTime(obj.getCron()));
                balanceBasicVo.setNextTime(obj.getNextControlTime());
                balanceBasicVo.setNetBalanceName(obj.getName());
                balanceBasicVo.setComputeTargetTemp(obj.getComputeTargetTemp());
                balanceBasicVo.setCompenSation(obj.getCompensation());
                balanceBasicVo.setComputeType(obj.getComputeType());
                balanceBasicVo.setStatus(obj.getStatus());
                balanceBasicVo.setComputeArea(obj.getComputeArea());
                balanceBasicVo.setRealArea(obj.getRealArea());
                balanceBasicVo.setComputeTargetAvg(obj.getComputeTargetAvg());
                balanceBasicVo.setRealTarget(obj.getRealTarget());
                balanceBasicVo.setComputeImbalance(obj.getComputeImbalance());
                balanceBasicVo.setRealImbalance(obj.getRealImbalance());
                balanceBasicVo.setSystemCount(obj.getSystemCount());
                balanceBasicVo.setCycle(obj.getCycle());
                return balanceBasicVo;
            }).collect(Collectors.toList());
            return Response.success(balanceBasicVosList);
        } catch (Exception e) {
            log.error("全网平衡基础信息查询发生错误" + e.getMessage());
            return Response.fail();
        }

    }

    @ApiOperation("查询全网平衡温度差距值饼图")
    @GetMapping("/selNetBalanceTempGap/{id}")
    public Response selNetBalanceTempGap(@PathVariable int id) throws IOException, MicroException {
        try {
            BalanceNet balanceNet = balanceNetService.getById(id);
            //定义实时数据参数条件
            List<BalanceSystemInfo> balanceSystemInfoList = balanceSystemInfoList(id);
            Map<Integer, String[]> maps = new HashMap<>();
            for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                maps.put(systemInfo.getRelevanceId(), new String[]{"T2g", "T2h"});
            }
            int level = 1;
            //获取实时数据
            List<PointCache> realValueList = redisCacheService.queryRealDataBySystems(maps, level);
            Map<Integer, List<PointCache>> pointGroupMap = realValueList.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
            List<BalanceTempGap> balanceTempGapList = new ArrayList<>();
            pointGroupMap.forEach((k, v) -> {
                BalanceTempGap balanceTempGap = new BalanceTempGap();
                balanceTempGap.setBalanceNetId(id);
                balanceTempGap.setSystemId(k);
                balanceTempGap.setComputeTargetAvg(balanceNet.getComputeTargetAvg());
                String tg = v.stream().filter(s -> "T2g".equals(s.getPointName())).collect(Collectors.toList()).get(0).getValue();
                String th = v.stream().filter(s -> "T2h".equals(s.getPointName())).collect(Collectors.toList()).get(0).getValue();
                float gapvalue = (Float.parseFloat(tg) + Float.parseFloat(th)) / 2 - Float.parseFloat(balanceNet.getComputeTargetAvg().toString());
                balanceTempGap.setGapValue(gapvalue);
                balanceTempGapList.add(balanceTempGap);
            });
            return Response.success(balanceTempGapList);
        } catch (Exception e) {
            log.error("查询全网平衡温度差距值饼图发生错误" + e.getMessage());
            return Response.fail();
        }

    }

    @ApiOperation("查询全网平衡阀门开度区间柱状图")
    @GetMapping("/selNetBalanceValveSection/{id}")
    public Response selNetBalanceValveSection(@PathVariable int id) throws IOException, MicroException {
        try {
//            BalanceNet balanceNet = balanceNetService.getById(id);
            List<BalanceSystemInfo> balanceSystemInfoList = balanceSystemInfoList(id);
            Map<Integer, String[]> maps = new HashMap<>();
            for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                maps.put(systemInfo.getRelevanceId(), new String[]{"CV1_U"});
            }
            int level = 1;
            //获取实时数据
            List<PointCache> realValueList = redisCacheService.queryRealDataBySystems(maps, level);
            CurveResponse curveResponse = new CurveResponse();
            List<Curve> curveArrayList = new ArrayList<>();
            for (PointCache p : realValueList) {
                Curve curve = new Curve();
                curve.setTime(System.currentTimeMillis());
                curve.setHeatSystemId(p.getRelevanceId());
                Map<String, BigDecimal> map = new HashMap<>(16);
                map.put(p.getPointName(), new BigDecimal(p.getValue()));
                curve.setPoint(map);
                curveArrayList.add(curve);
            }
            curveResponse.setCurves(curveArrayList);
            return Response.success(curveResponse);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询全网平衡实时曲线")
    @PostMapping("/selNetBalanceTargetCurve")
    public Response selNetBalanceTargetCurve(@RequestBody BalanceCurveDto balanceCurveDto) {
        try {
            QueryWrapper<BalanceTargetHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("createTime", balanceCurveDto.getStartTime(), balanceCurveDto.getEndTime());
            queryWrapper.eq("balanceNetId", balanceCurveDto.getId());
            List<BalanceTargetHistory> balanceTargetHistoriesList = balanceTargetHistoryService.list(queryWrapper);
            CurveResponse curveResponse = new CurveResponse();
            List<Curve> curveList = balanceTargetHistoriesList.stream().map(s -> {
                Curve curve = new Curve();
                curve.setTime(Timestamp.valueOf(s.getCreateTime()).getTime());
                curve.setHeatSystemId(s.getBalanceNetId());
                Map<String, BigDecimal> map = new HashMap<>();
                map.put("targetTemp", s.getTargetTemp());
                map.put("imbalance", s.getImbalance());
                map.put("realtargetTemp", s.getRealtargetTemp());
                map.put("realImbalance", s.getRealImbalance());
                curve.setPoint(map);
                return curve;
            }).collect(Collectors.toList());
            curveResponse.setCurves(curveList);
            return Response.success(curveResponse);
        } catch (Exception e) {
            log.error("查询全网平衡实时曲线请求错误：{}", e.getMessage());
            return Response.fail();
        }
    }

    public List<BalanceSystemInfo> balanceSystemInfoList(int id) {
        QueryWrapper<BalanceMembers> balanceMembersQueryWrapper = new QueryWrapper<>();
        balanceMembersQueryWrapper.eq("b.balanceNetId", id);
        return balanceMembersService.balanceSystemInfo(balanceMembersQueryWrapper);
    }

    @ApiOperation("查询全网平衡散点图信息")
    @GetMapping("queryScatterDiagram")
    public Response queryScatterDiagram() {
        List<QueryScatterDiagramResponse> response = new ArrayList<>();
        try {
            List<BalanceNet> netList = balanceNetService.list();
            if(netList.size()<=0){
                return Response.success(response);
            }
            List<BalanceMemberResponse> list = balanceMembersMapper.queryBalanceMemberResponse(new QueryWrapper());
            if(list.size()<=0){
                return Response.success(response);
            }
            Map<Integer, String[]> maps = new HashMap<>();
            for (BalanceMemberResponse systemInfo : list) {
                maps.put(systemInfo.getRelevanceId(), new String[]{"CV1_U", "T2g", "T2h", "SEC_AVG_DIFF"});
            }
            //获取实时数据
            List<PointCache> realValueList = redisCacheService.queryRealOnlyValue(maps, TreeLevel.HeatSystem.level());
            Map<Integer, List<BalanceMemberResponse>> groupMap = list.stream().collect(Collectors.groupingBy(s -> s.getBalanceNetId()));
            groupMap.forEach((key, value) -> {
                QueryScatterDiagramResponse obj = new QueryScatterDiagramResponse();
                BalanceNet net = netList.stream().filter(s -> s.getId() == key).findFirst().orElse(null);
                if (net != null) {
                    obj.setNetBalanceName(net.getName());
                }
                for (BalanceMemberResponse member : value) {
                    List<PointCache> memberRealDataList = realValueList.stream().filter(s -> s.getRelevanceId() == member.getRelevanceId()).collect(Collectors.toList());
                    if (memberRealDataList != null && memberRealDataList.size() > 0) {
                        String diffValue = memberRealDataList.stream().filter(s -> Objects.equals(s.getPointName(), "SEC_AVG_DIFF")).findFirst().orElse(null).getValue();
                        String valveOpening = memberRealDataList.stream().filter(s -> Objects.equals(s.getPointName(), "CV1_U")).findFirst().orElse(null).getValue();
                        String tg = memberRealDataList.stream().filter(s -> Objects.equals(s.getPointName(), "T2g")).findFirst().orElse(null).getValue();
                        String th = memberRealDataList.stream().filter(s -> Objects.equals(s.getPointName(), "T2h")).findFirst().orElse(null).getValue();
                        member.setDiffValue(diffValue);
                        member.setValveOpening(valveOpening);
                        member.setTg(tg);
                        member.setTh(th);
                    }
                }
                obj.setResponses(value);
                response.add(obj);
            });
            return Response.success(response);
        } catch (Exception e) {
            log.error("全网平衡散点图查询内部错误……………………{}",e.getMessage());
            return Response.success(response);
        }
    }

}
