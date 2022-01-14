package com.bmts.heating.bussiness.netBalance.joggle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.netBalance.handler.ComputeJob;
import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.balance.response.BalanceNetDto;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.quartz.JobTimeStageType;
import com.bmts.heating.commons.container.quartz.service.impl.JobServiceImpl;
import com.bmts.heating.commons.db.mapper.LogOperationBalanceMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import com.bmts.heating.commons.basement.model.balance.response.BalanceNetDto;

@Api(tags = "全网平衡")
@Slf4j
@RestController
@RequestMapping("netbalancebase")
public class NetBalanceBaseJoggle {

    @Autowired
    private BalanceNetService balanceNetService;

    @Autowired
    private BalanceNetLimitService balanceNetLimitService;
    @Autowired
    private BalanceCompensationService balanceCompensationService;
    @Autowired
    private BalanceMembersService balanceMembersService;
    @Autowired
    private LogOperationBalanceService logOperationBalanceService;
    @Autowired
    private LogOperationBalanceMapper logOperationBalanceMapper;
    @Autowired
    JobServiceImpl jobService;
    @Autowired
    AuthrityService authrityService;
    @Autowired
    StationFirstNetBaseViewService stationFirstNetBaseViewService;

    @ApiOperation("新增-修改-网")
    @PostMapping("/edit")
    public Response insert(@RequestBody BalanceNet balanceNet) {

        if (balanceNetService.saveOrUpdate(balanceNet)) {
            boolean computeResult = jobService.addJob(balanceNet.getId() + "_" + balanceNet.getName() + "_compute", "Balance", 1, ComputeJob.class, null, JobTimeStageType.MINUTE.type());
            if (computeResult) {
                return Response.success(balanceNet.getId());
            } else {
                return Response.fail();
            }
        } else {
            return Response.fail();
        }
    }


    @ApiOperation("删除-网及其扩展字段")
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response delete(@PathVariable Integer id) {
        List<BalanceNetLimit> balanceNetLimit = balanceNetLimitService.list(Wrappers.<BalanceNetLimit>lambdaQuery().eq(BalanceNetLimit::getBalanceNetId, id));
        List<BalanceMembers> balanceMembers = balanceMembersService.list(Wrappers.<BalanceMembers>lambdaQuery().eq(BalanceMembers::getBalanceNetId, id));
        if (balanceNetLimit.stream().count() > 0) {
            Boolean result = balanceNetLimitService.remove(Wrappers.<BalanceNetLimit>lambdaQuery().eq(BalanceNetLimit::getBalanceNetId, id));
            System.out.println("BalanceNetLimit表s删除状态：" + result);
        } else {
            System.out.println("BalanceNetLimit表中无全网平衡id为:" + id + "的数据信息");
        }
        if (balanceMembers.stream().count() > 0) {
            Boolean result = balanceMembersService.remove(Wrappers.<BalanceMembers>lambdaQuery().eq(BalanceMembers::getBalanceNetId, id));
            System.out.println("BalanceMembers表s删除状态：" + result);
        } else {
            System.out.println("BalanceMembers表中无全网平衡id为:" + id + "的数据信息");
        }
        if (balanceNetService.removeById(id)) {
            return Response.success();
        } else {
            return Response.fail();
        }


//        List<BalanceMembers> list = balanceMembersService.list(Wrappers.<BalanceMembers>lambdaQuery().eq(BalanceMembers::getBalanceNetId, id));
//        list.forEach(e -> e.setBalanceNetId(null));
//        QueryWrapper<BalanceNetLimit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("balanceNetId",id);
//        Boolean result =  balanceNetLimitService.remove(queryWrapper);
//        if(!result){
//            throw new RuntimeException("删除失败，请刷新后重试");
//        }
//        balanceMembersService.updateBatchById(list);
//        if(balanceNetService.removeById(id)){
//            return Response.success();
//        }
//        throw new RuntimeException("删除失败，请刷新后重试");
    }

    @ApiOperation("单体查询")
    @GetMapping("/{id}")
    public Response query(@PathVariable Integer id) {
        if (id == null) {
            return Response.paramError();
        }
        BalanceNet balanceNet = balanceNetService.getById(id);
        BalanceNetDto balanceNetDto = new BalanceNetDto();
        balanceNetDto.setBalanceNet(balanceNet);
        balanceNetDto.setBalanceNetLimits(queryAll(id));
        return Response.success(balanceNetDto);
    }

    @ApiOperation("查询全部")
    @GetMapping("/all/{userId}")
    public Response queryNetBalance(@PathVariable Integer userId) {
        if (userId == -1) {
            return Response.success(balanceNetService.list());
        }
        try {
            UserDataPerms userDataPerms = authrityService.queryStations(userId);
            List<Object> stationIds = Arrays.asList(userDataPerms.getStations().toArray());
            List<StationFirstNetBaseView> stationFirstNetBaseViews = stationFirstNetBaseViewService.list(Wrappers.<StationFirstNetBaseView>lambdaQuery().in(StationFirstNetBaseView::getHeatTransferStationId, stationIds).ne(StationFirstNetBaseView::getNumber, 0));
            List<Integer> systemIds = stationFirstNetBaseViews.stream().map(s -> s.getHeatSystemId()).collect(Collectors.toList());
            List<BalanceMembers> balanceMembersList = balanceMembersService.list(Wrappers.<BalanceMembers>lambdaQuery().in(BalanceMembers::getRelevanceId, systemIds).eq(BalanceMembers::getStatus, true));
            Map<Integer, List<BalanceMembers>> balanceInfo = balanceMembersList.stream().collect(Collectors.groupingBy(s -> s.getBalanceNetId()));
            List<Integer> netBalanceIds = new ArrayList<>();
            balanceInfo.forEach((key, value) -> netBalanceIds.add(key));
            return Response.success(balanceNetService.list(Wrappers.<BalanceNet>lambdaQuery().in(BalanceNet::getId, netBalanceIds)));
        } catch (Exception e){
            log.error("查询全网平衡全部虚拟网信息内部错误NetBalanceBaseJoggle->queryNetBalance,异常内容-------{}",e.getMessage());
            return Response.success();
        }
    }

    private List<BalanceNetLimit> queryAll(Integer id) {
        LambdaQueryWrapper<BalanceNetLimit> wrapper = new QueryWrapper<BalanceNetLimit>().lambda().eq(BalanceNetLimit::getBalanceNetId, id);
        return balanceNetLimitService.list(wrapper);
    }

    @ApiOperation("查询全部")
    @PostMapping("/config/limit")
    public Response insert(@RequestBody List<BalanceNetLimit> limits) {
        return Response.success(balanceNetLimitService.saveOrUpdateBatch(limits));
    }

    @ApiOperation("大类补偿值下拉菜单")
    @GetMapping("/select")
    public Response list() {
        return Response.success(balanceCompensationService.list());
    }

    @ApiOperation("添加全网平衡操作记录信息")
    @PostMapping("/insertLogOperationBalance")
    public Response insertLog(@RequestBody LogOperationBalance dto) {
        try {
            boolean result = logOperationBalanceService.save(dto);
            if (result) {
                return Response.success();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @ApiOperation("添加全网平衡操作记录信息")
    @PostMapping("/insertLogById")
    public Response insertLogById(@RequestBody BalanceLogDto dto) {
        try {
            int result = logOperationBalanceMapper.insertLogById(dto);
            if (result > 0) {
                return Response.success();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("批量添加全网平衡操作记录信息")
    @PostMapping("/insertLogByIds")
    public Response insertLogByIds(@RequestBody BalanceLogDto dto) {
        try {
            int result = logOperationBalanceMapper.insertLogByIds(dto);
            if (result > 0) {
                return Response.success();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }
}
