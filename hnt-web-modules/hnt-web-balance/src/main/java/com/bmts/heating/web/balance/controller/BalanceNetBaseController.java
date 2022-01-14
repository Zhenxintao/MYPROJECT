package com.bmts.heating.web.balance.controller;

import com.bmts.heating.commons.basement.model.balance.response.BalanceNetDto;
import com.bmts.heating.commons.basement.model.db.entity.BalanceCompensation;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNet;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceBasicVo;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceTempGap;
import com.bmts.heating.commons.entiy.balance.pojo.QueryScatterDiagramResponse;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetAddDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetSystemListDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceTableQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.entiy.gathersearch.request.BalanceCurveDto;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.BalanceRealValue;
import com.bmts.heating.commons.entiy.gathersearch.response.history.CurveResponse;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.common.HttpIpUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.web.balance.service.BalanceIndexInfoService;
import com.bmts.heating.web.balance.service.BalanceNewBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Api(tags = "全网平衡")
@Slf4j
@RestController
@RequestMapping("netbalancebase")
public class BalanceNetBaseController {
    @Autowired
    private BalanceNewBaseService balanceNewBaseService;

    @Autowired
    private BalanceIndexInfoService balanceIndexInfoService;

    @ApiOperation(value = "新增-修改-网")
    @PostMapping
    public Response insert(@RequestBody BalanceNetAddDto balanceNetAddDto, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (balanceNetAddDto.getBalanceNet().getId() == null) {
            if (StringUtils.isNotBlank(userName)) {
                balanceNetAddDto.getBalanceNet().setCreateUser(userName);
                balanceNetAddDto.getBalanceNet().setCreateTime(LocalDateTime.now());
            }
        } else {
            if (StringUtils.isNotBlank(userName)) {
                balanceNetAddDto.getBalanceNet().setUpdateUser(userName);
                balanceNetAddDto.getBalanceNet().setUpdateTime(LocalDateTime.now());
            }
        }
        Response response = balanceNewBaseService.insert(balanceNetAddDto);
        LogOperationBalance log = new LogOperationBalance();
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.setUserName(userName);
            log.setCreateTime(LocalDateTime.now());
            log.setIpAddress(HttpIpUtil.getRealIp(request));
            log.setButton("添加或修改热网");
            log.setDescription("添加-修改热网-" + balanceNetAddDto.getBalanceNet().getName() + "-成功！");
            log.setModule("热网配置模块");
            balanceNewBaseService.insertLog(log);
            return response;
        } else {
            log.setUserName(userName);
            log.setCreateTime(LocalDateTime.now());
            log.setIpAddress(HttpIpUtil.getRealIp(request));
            log.setButton("添加或修改热网");
            log.setDescription("添加-修改热网-" + balanceNetAddDto.getBalanceNet().getName() + "-失败！");
            log.setModule("热网配置模块");
            balanceNewBaseService.insertLog(log);
            return response;
        }
    }

    @ApiOperation("删除-网及其扩展字段")
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id, HttpServletRequest request) {
        Response response = Response.fail();
        if (this.netBalanceStop(id, request).getCode() == 200) {
            response = balanceNewBaseService.delete(id);
        }
        String userName = JwtUtils.getUserName(request);
        BalanceLogDto log = new BalanceLogDto();
        log.setId(id);
        log.setUserName(userName);
        log.setCreateTime(LocalDateTime.now());
        log.setIpAddress(HttpIpUtil.getRealIp(request));
        log.setButton("删除热网");
        log.setDescriptionHead("删除热网-");
        log.setModule("热网配置");
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.setDescriptionTrail("-成功！");
            balanceNewBaseService.insertLogById(log);
            return response;
        } else {
            log.setDescriptionTrail("-失败！");
            balanceNewBaseService.insertLogById(log);
            return response;
        }
    }

    @ApiOperation(value = "单体查询", response = BalanceNetDto.class)
    @GetMapping("/{id}")
    public Response query(@PathVariable int id) {
        return balanceNewBaseService.query(id);
    }

    @ApiOperation(value = "查询全部", response = BalanceNet.class)
    @GetMapping("/all")
    public Response query(HttpServletRequest httpServletRequest) {
        Integer userId = JwtUtils.getUserId(httpServletRequest);
        return balanceNewBaseService.query(userId);
    }


    @ApiOperation(value = "系统列表")
    @ApiParam("id: 网Id")
    @GetMapping("list/System")
    public Response querySystemList(BalanceNetSystemListDto dto) {
        return balanceNewBaseService.querySystemList(dto);
    }

    @ApiOperation(value = "全网平衡首页表格", response = BalanceRealValue.class)
    @PostMapping("table")
    public Response table(@RequestBody BalanceTableQueryDto dto) {
        return Response.success(balanceNewBaseService.table(dto));
    }

    @ApiOperation(value = "查询全网平衡信息:参数id为0获取全部站点信息", response = BalanceBasicVo.class)
    @GetMapping("selNetBalanceInfo/{id}")
    public Response selNetBalanceInfo(@PathVariable int id) {
        return balanceNewBaseService.selNetBalanceInfo(id);
    }

    //查询全网平衡温度差距值饼图
    @ApiOperation(value = "查询全网平衡温度差距值饼图", response = BalanceTempGap.class)
    @GetMapping("selNetBalanceTempGap/{id}")
    public Response selNetBalanceTempGap(@PathVariable int id) {
        return balanceIndexInfoService.selNetBalanceTempGap(id);
    }

    //查询全网平衡阀门开度区间柱状图
    @ApiOperation(value = "查询全网平衡阀门开度区间柱状图", response = CurveResponse.class)
    @GetMapping("selNetBalanceValveSection/{id}")
    public Response selNetBalanceValveSection(@PathVariable int id) {
        return balanceIndexInfoService.selNetBalanceValveSection(id);
    }

    //查询全网平衡实时曲线
    @ApiOperation(value = "查询全网平衡实时曲线", response = CurveResponse.class)
    @PostMapping("selNetBalanceTargetCurve")
    public Response selNetBalanceTargetCurve(@RequestBody BalanceCurveDto balanceCurveDto) {
        return balanceIndexInfoService.selNetBalanceTargetCurve(balanceCurveDto);
    }

    //全网平衡启动
    @ApiOperation("全网平衡启动")
    @GetMapping("netBalanceStart/{id}")
    public Response netBalanceStart(@PathVariable int id, HttpServletRequest request) {
        Response response = balanceIndexInfoService.netBalanceStart(id);
        String userName = JwtUtils.getUserName(request);
        BalanceLogDto log = new BalanceLogDto();
        log.setId(id);
        log.setUserName(userName);
        log.setCreateTime(LocalDateTime.now());
        log.setIpAddress(HttpIpUtil.getRealIp(request));
        log.setButton("启动全网平衡");
        log.setDescriptionHead("启动全网平衡-");
        log.setModule("全网平衡首页");
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.setDescriptionTrail("-成功！");
            balanceNewBaseService.insertLogById(log);
            return response;
        } else {
            log.setDescriptionTrail("-失败！");
            balanceNewBaseService.insertLogById(log);
            return response;
        }
    }

    //全网平衡停止
    @ApiOperation("全网平衡停止")
    @GetMapping("netBalanceStop/{id}")
    public Response netBalanceStop(@PathVariable int id, HttpServletRequest request) {
        Response response = balanceIndexInfoService.netBalanceStop(id);
        String userName = JwtUtils.getUserName(request);
        BalanceLogDto log = new BalanceLogDto();
        log.setId(id);
        log.setUserName(userName);
        log.setCreateTime(LocalDateTime.now());
        log.setIpAddress(HttpIpUtil.getRealIp(request));
        log.setButton("停止全网平衡");
        log.setDescriptionHead("停止全网平衡-");
        log.setModule("全网平衡首页");
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.setDescriptionTrail("-成功！");
            balanceNewBaseService.insertLogById(log);
            return response;
        } else {
            log.setDescriptionTrail("-失败！");
            balanceNewBaseService.insertLogById(log);
            return response;
        }
    }

    @ApiOperation(value = "大类补偿值下拉菜单", response = BalanceCompensation.class)
    @GetMapping("/select")
    public Response list() {
        return Response.success(balanceNewBaseService.list());
    }

    @ApiOperation(value = "查询全网平衡散点图信息", response = QueryScatterDiagramResponse[].class)
    @GetMapping("/queryScatterDiagram")
    @PassToken
    public Response queryScatterDiagram(){
        return Response.success(balanceIndexInfoService.queryScatterDiagram());
    }
}

