package com.bmts.heating.web.scada.controller.issue;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.common.HttpIpUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Api(tags = "控制下发")
@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @ApiOperation("下发二次补水变频泵数据")
    @PostMapping("/bpMode")
    public Response bpMode(@RequestBody BPModeDto param, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            param.setUserName(userName);
        } else {
            return Response.fail("没有查询到该用户信息！无法控制下发！");
        }
        String realIp = HttpIpUtil.getRealIp(request);
        if (StringUtils.isNotBlank(realIp)) {
            param.setIp(realIp);
        }
        return issueService.bpModeIssue(param);
    }

    @ApiOperation("下发一次调节阀数据")
    @PostMapping("/cvMode")
    public Response cvMode(@RequestBody CVModeDto param, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            param.setUserName(userName);
        } else {
            return Response.fail("没有查询到该用户信息！无法控制下发！");
        }
        String realIp = HttpIpUtil.getRealIp(request);
        if (StringUtils.isNotBlank(realIp)) {
            param.setIp(realIp);
        }
        return issueService.cvModeIssue(param);
    }

    @ApiOperation("下发二次循环泵数据")
    @PostMapping("/xpMode")
    public Response xpMode(@RequestBody XPModeDto param, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            param.setUserName(userName);
        } else {
            return Response.fail("没有查询到该用户信息！无法控制下发！");
        }
        String realIp = HttpIpUtil.getRealIp(request);
        if (StringUtils.isNotBlank(realIp)) {
            param.setIp(realIp);
        }
        return issueService.xpModeIssue(param);
    }

    @ApiOperation("下发泄压阀数据")
    @PostMapping("/xsvMode")
    public Response xsvMode(@RequestBody XSVModeDto param, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            param.setUserName(userName);
        } else {
            return Response.fail("没有查询到该用户信息！无法控制下发！");
        }
        String realIp = HttpIpUtil.getRealIp(request);
        if (StringUtils.isNotBlank(realIp)) {
            param.setIp(realIp);
        }
        return issueService.xsvModeIssue(param);
    }

}
