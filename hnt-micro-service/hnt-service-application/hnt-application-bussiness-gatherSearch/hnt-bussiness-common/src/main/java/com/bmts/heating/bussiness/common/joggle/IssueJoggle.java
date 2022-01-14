package com.bmts.heating.bussiness.common.joggle;

import com.bmts.heating.bussiness.common.service.BPModeService;
import com.bmts.heating.bussiness.common.service.CVModeService;
import com.bmts.heating.bussiness.common.service.XPModeService;
import com.bmts.heating.bussiness.common.service.XSVModeService;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api("控制下发管理")
@Slf4j
@RestController
@RequestMapping("/issue")
public class IssueJoggle {

    @Autowired
    private BPModeService bpModeService;

    @Autowired
    private CVModeService cvModeService;

    @Autowired
    private XPModeService xpModeService;

    @Autowired
    private XSVModeService xsvModeService;


    @ApiOperation("下发二次补水变频泵数据")
    @PostMapping("/bpMode")
    public Response bpMode(@RequestBody BPModeDto dto) {
        if (dto.getSystemId() == null) {
            return Response.fail("机组参数错误！");
        }
        return bpModeService.down(dto);
    }

    @ApiOperation("下发一次调节阀数据")
    @PostMapping("/cvMode")
    public Response cvMode(@RequestBody CVModeDto dto) {
        if (dto.getSystemId() == null) {
            return Response.fail("机组参数错误！");
        }
        if (dto.getOptions() == null) {
            return Response.fail("参数错误！");
        }
        return cvModeService.down(dto);
    }

    @ApiOperation("下发二次循环泵数据")
    @PostMapping("/xpMode")
    public Response xpMode(@RequestBody XPModeDto dto) {
        if (dto.getSystemId() == null) {
            return Response.fail("机组参数错误！");
        }
        return xpModeService.down(dto);
    }

    @ApiOperation("下发泄压阀数据")
    @PostMapping("/xsvMode")
    public Response xsvMode(@RequestBody XSVModeDto dto) {
        if (dto.getSystemId() == null) {
            return Response.fail("机组参数错误！");
        }
        if (dto.getOptions() == null) {
            return Response.fail("参数错误！");
        }
        return xsvModeService.down(dto);
    }


}
