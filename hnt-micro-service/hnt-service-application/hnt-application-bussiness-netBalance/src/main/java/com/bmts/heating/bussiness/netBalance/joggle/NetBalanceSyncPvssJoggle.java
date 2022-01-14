package com.bmts.heating.bussiness.netBalance.joggle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.netBalance.handler.ComputeJob;
import com.bmts.heating.commons.basement.model.balance.response.BalanceNetDto;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.quartz.JobTimeStageType;
import com.bmts.heating.commons.container.quartz.service.impl.JobServiceImpl;
import com.bmts.heating.commons.db.mapper.LogOperationBalanceMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.entiy.balance.pojo.syncPvss.BalanceMembersInfo;
import com.bmts.heating.commons.entiy.balance.pojo.syncPvss.BalanceNetInfo;
import com.bmts.heating.commons.entiy.balance.pojo.syncPvss.UpdateBalanceMembersInfo;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bmts.heating.commons.utils.restful.Response.*;


@Api(tags = "全网平衡同步PVSS")
@Slf4j
@RestController
@RequestMapping("netBalanceSyncPvss")
public class NetBalanceSyncPvssJoggle {

    @Autowired
    private BalanceNetService balanceNetService;
    @Autowired
    private BalanceMembersService balanceMembersService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private NetBalanceBaseJoggle netBalanceBaseJoggle;
    @Autowired
    private NetBalanceJoggle netBalanceJoggle;
    @Autowired
    private JobJoggle jobJoggle;

    @ApiOperation("新增网")
    @PostMapping("/insertNetBalance")
    public Response insertNetBalance(@RequestBody BalanceNetInfo balanceNetInfo) {
        try {
            if (balanceNetInfo != null) {
                QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syncNetBalanceId", balanceNetInfo.getSyncNetBalanceId());
                BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
                if (balanceNet != null) {
                    log.error("新增全网平衡信息失败，平台已存在该全网平衡信息，编号id为----{}", balanceNetInfo.getSyncNetBalanceId());
                    return fail("新增全网平衡信息失败，平台已存在该全网平衡信息，编号id为----" + balanceNetInfo.getSyncNetBalanceId());
                } else {
                    BalanceNet balanceNetNew = new BalanceNet();
                    BeanUtils.copyProperties(balanceNetInfo, balanceNetNew);
                    balanceNetNew.setCreateUser("PVSS");
                    balanceNetNew.setCreateTime(LocalDateTime.now());
                    if (netBalanceBaseJoggle.insert(balanceNetNew).getCode() == ResponseCode.SUCCESS.getCode()) {
                        return success();
                    }
                }
            }
            return fail("新增全网平衡信息失败，参数值为null");
        } catch (Exception e) {
            log.error("PVSS同步全网平衡新增网接口失败----{}", e.getMessage());
            return fail("PVSS同步全网平衡新增网接口失败" + e.getMessage());
        }
    }


    @ApiOperation("删除网")
    @GetMapping("/deleteNetBalance")
    public Response deleteNetBalance(@RequestParam String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syncNetBalanceId", id);
                BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
                if (balanceNet == null) {
                    log.error("删除失败，平台无该全网平衡编号id----{}", id);
                    return fail("删除失败，平台无该全网平衡编号id----" + id);
                } else {
                    if (netBalanceBaseJoggle.delete(balanceNet.getId()).getCode() == ResponseCode.SUCCESS.getCode()) {
                        return success();
                    }
                }

            }
            return fail("删除失败，参数值为空字符串或null");
        } catch (Exception e) {
            log.error("PVSS同步全网平衡删除网接口失败----{}", e.getMessage());
            return fail("PVSS同步全网平衡删除网接口失败---" + e.getMessage());
        }
    }

    @ApiOperation("修改网")
    @PostMapping("/updateNetBalance")
    public Response updateNetBalance(@RequestBody BalanceNetInfo balanceNetInfo) {
        try {
            if (balanceNetInfo != null) {
                QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syncNetBalanceId", balanceNetInfo.getSyncNetBalanceId());
                BalanceNet balanceNetOne = balanceNetService.getOne(queryWrapper);
                if (balanceNetOne == null) {
                    log.error("修改失败平台无该全网平衡编号id----{}", balanceNetInfo.getSyncNetBalanceId());
                    return fail("修改失败平台无该全网平衡编号id----" + balanceNetInfo.getSyncNetBalanceId());
                } else {
                    BeanUtils.copyProperties(balanceNetInfo, balanceNetOne);
                    balanceNetOne.setUpdateUser("PVSS");
                    balanceNetOne.setUpdateTime(LocalDateTime.now());
                    if (netBalanceBaseJoggle.insert(balanceNetOne).getCode() == ResponseCode.SUCCESS.getCode()) {
                        return success();
                    }
                }
            }
            return fail("修改失败，参数值为null");
        } catch (Exception e) {
            log.error("PVSS同步全网平衡修改网接口失败----{}", e.getMessage());
            return fail("PVSS同步全网平衡修改网接口失败---" + e.getMessage());
        }
    }

//    @ApiOperation("新增网及系统配置信息")
//    @PostMapping("/insertBalanceMembersInfo")
//    public Response insertBalanceMembersInfo(@RequestBody BalanceMembersInfo balanceMembersInfo) {
//        try {
//            if (balanceMembersInfo != null) {
//                QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("syncNetBalanceId", balanceMembersInfo.getSyncNetBalanceId());
//                BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
//                if (balanceNet == null) {
//                    log.error("新增网及系统配置信息失败，新平台未存在该网信息，网编号id为----{}", balanceMembersInfo.getSyncNetBalanceId());
//                    return fail("新增网及系统配置信息失败，新平台未存在该网信息，网编号id为----" + balanceMembersInfo.getSyncNetBalanceId());
//                } else {
//                    QueryWrapper<StationFirstNetBaseView> queryStation = new QueryWrapper<>();
//                    queryStation.in("systemSyncNum", balanceMembersInfo.getRelevanceIds());
//                    queryStation.select("heatSystemId", "systemSyncNum");
//                    List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list(queryStation);
//                    List<BalanceMembers> balanceMembersList = new ArrayList<>();
//                    for (StationFirstNetBaseView s : list) {
//                        balanceMembersList.add(new BalanceMembers() {{
//                            setBalanceNetId(Integer.valueOf(balanceNet.getId()));
//                            setLevel(1);
//                            setRelevanceId(s.getHeatSystemId());
//                            setCompensation(new BigDecimal(0));
//                            setBalanceCompensationId(0);
//                            setStatus(true);
//                            setControlType(0);
//                        }});
//                    }
//                    if (netBalanceJoggle.saveMemebers(balanceMembersList).getCode() == ResponseCode.SUCCESS.getCode()) {
//                        return success();
//                    }
//                }
//            }
//            return fail("新增网及系统配置信息失败，参数值为null");
//        } catch (Exception e) {
//            log.error("PVSS同步全网平衡新增网及系统配置信息失败----{}", e.getMessage());
//            return fail("PVSS同步全网平衡新增网及系统配置信息失败" + e.getMessage());
//        }
//    }

    @ApiOperation("新增网及系统配置信息")
    @PostMapping("/insertBalanceMembersInfo")
    public Response insertBalanceMembersInfo(@RequestBody BalanceMembersInfo balanceMembersInfo) {
        try {
            if (balanceMembersInfo != null) {
                QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syncNetBalanceId", balanceMembersInfo.getSyncNetBalanceId());
                BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
                if (balanceNet == null) {
                    log.error("新增网及系统配置信息失败，新平台未存在该网信息，网编号id为----{}", balanceMembersInfo.getSyncNetBalanceId());
                    return fail("新增网及系统配置信息失败，新平台未存在该网信息，网编号id为----" + balanceMembersInfo.getSyncNetBalanceId());
                } else {
                    QueryWrapper<BalanceMembers> balanceMembersQueryWrapper = new QueryWrapper<>();
                    balanceMembersQueryWrapper.eq("balanceNetId", balanceNet.getId());
                    balanceMembersService.remove(balanceMembersQueryWrapper);
                    QueryWrapper<StationFirstNetBaseView> queryStation = new QueryWrapper<>();
                    queryStation.in("systemSyncNum", balanceMembersInfo.getRelevanceIds());
                    queryStation.select("heatSystemId", "systemSyncNum");
                    List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list(queryStation);
                    if (list.size()>0) {
                        List<BalanceMembers> balanceMembersList = new ArrayList<>();
                        for (StationFirstNetBaseView s : list) {
                            balanceMembersList.add(new BalanceMembers() {{
                                setBalanceNetId(Integer.valueOf(balanceNet.getId()));
                                setLevel(1);
                                setRelevanceId(s.getHeatSystemId());
                                setCompensation(new BigDecimal(0));
                                setBalanceCompensationId(0);
                                setStatus(true);
                                setControlType(0);
                            }});
                        }
                        if (netBalanceJoggle.saveMemebers(balanceMembersList).getCode() == ResponseCode.SUCCESS.getCode())
                            return success();
                        return fail();
                    }else {
                        return fail("新增网及系统信息失败！未查询到系统信息");
                    }
                }
            }
            return fail("新增网及系统配置信息失败，参数值为null");
        } catch (Exception e) {
            log.error("PVSS同步全网平衡新增网及系统配置信息失败----{}", e.getMessage());
            return fail("PVSS同步全网平衡新增网及系统配置信息失败" + e.getMessage());
        }
    }

    @ApiOperation("修改网及系统配置信息")
    @PostMapping("/updateBalanceMembersInfo")
    public Response updateBalanceMembersInfo(@RequestBody UpdateBalanceMembersInfo updateBalanceMembersInfo) {
        if (!Objects.equals(updateBalanceMembersInfo.getOriginalSyncNetBalanceId(), updateBalanceMembersInfo.getProceedSyncNetBalanceId())) {
            QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
            List<Integer> netBalanceIds = new ArrayList<>();
            netBalanceIds.add(Integer.valueOf(updateBalanceMembersInfo.getOriginalSyncNetBalanceId()));
            netBalanceIds.add(Integer.valueOf(updateBalanceMembersInfo.getProceedSyncNetBalanceId()));
            queryWrapper.in("syncNetBalanceId", netBalanceIds);
            List<BalanceNet> balanceNetList = balanceNetService.list(queryWrapper);
            if (balanceNetList.size() == 2) {
                Integer getOriginalSyncNetBalanceId = balanceNetList.stream().filter(s -> Objects.equals(s.getSyncNetBalanceId(), updateBalanceMembersInfo.getOriginalSyncNetBalanceId())).findFirst().orElse(null).getId();
                Integer getProceedSyncNetBalanceId = balanceNetList.stream().filter(s -> Objects.equals(s.getSyncNetBalanceId(), updateBalanceMembersInfo.getProceedSyncNetBalanceId())).findFirst().orElse(null).getId();
                QueryWrapper<StationFirstNetBaseView> queryStation = new QueryWrapper<>();
                queryStation.in("systemSyncNum", updateBalanceMembersInfo.getRelevanceIds());
                queryStation.select("heatSystemId", "systemSyncNum");
                List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list(queryStation);
                QueryWrapper<BalanceMembers> membersQueryWrapper = new QueryWrapper<>();
                membersQueryWrapper.in("relevanceId", list.stream().map(s -> s.getHeatSystemId()).collect(Collectors.toList()));
                membersQueryWrapper.eq("balanceNetId", getOriginalSyncNetBalanceId);
                List<BalanceMembers> balanceMembersList = balanceMembersService.list(membersQueryWrapper);
                balanceMembersList.forEach(s -> s.setBalanceNetId(getProceedSyncNetBalanceId));
                if (balanceMembersService.updateBatchById(balanceMembersList)) return success();
                else return fail();
            } else {
                log.error("未查询到参数编号Id的两个热网，请再次确认热网信息是否同步!热网编号Id为-----{},{}", updateBalanceMembersInfo.getOriginalSyncNetBalanceId(), updateBalanceMembersInfo.getProceedSyncNetBalanceId());
                return fail("未查询到参数编号Id的两个热网，请再次确认热网信息是否同步!----热网编号Id为:" + updateBalanceMembersInfo.getOriginalSyncNetBalanceId() + "," + updateBalanceMembersInfo.getProceedSyncNetBalanceId());
            }
        }
        return fail("修改系统归属失败！原始热网与需修改热网编号Id相同！");
    }

    @ApiOperation("启动全网平衡")
    @GetMapping("/startNetBalance")
    public Response startNetBalance(@RequestParam String id) {
        QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("syncNetBalanceId", id);
        BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
        if (balanceNet != null && balanceNet.getCycle()>0) {
            if (jobJoggle.start(balanceNet.getId()).getCode() == ResponseCode.SUCCESS.getCode()) {
                return success();
            } else {
                return fail("全网平衡定时启动失败,网名称为:" + balanceNet.getName());
            }
        }
        else if(balanceNet.getCycle()<=0){
            log.error("该热网循环周期为0，无法启动全网平衡，网编号id为----{};网名称为---{}", id, balanceNet.getName());
            return fail("该热网循环周期为0，无法启动全网平衡，网编号id为:" + id + ";网名称为:" + balanceNet.getName());
        }
        else {
            log.error("新平台数据库内未查询该全网平衡信息，无法启动全网平衡，网编号id为----{};网名称为---{}", id, balanceNet.getName());
            return fail("新平台数据库内未查询该全网平衡信息，无法启动全网平衡，网编号id为:" + id + ";网名称为:" + balanceNet.getName());
        }
    }

    @ApiOperation("停止全网平衡")
    @GetMapping("/stopNetBalance")
    public Response stopNetBalance(@RequestParam String id) {
        QueryWrapper<BalanceNet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("syncNetBalanceId", id);
        BalanceNet balanceNet = balanceNetService.getOne(queryWrapper);
        if (balanceNet != null) {
            if (jobJoggle.delete(balanceNet.getId()).getCode() == ResponseCode.SUCCESS.getCode()) {
                return success();
            } else {
                return fail();
            }
        } else {
            log.error("新平台数据库内未查询该全网平衡信息，无法停止全网平衡，网编号id为----{};网名称为---{}", id, balanceNet.getName());
            return fail("新平台数据库内未查询该全网平衡信息，无法停止全网平衡，网编号id为:" + id + ";网名称为:" + balanceNet.getName());
        }
    }

}
