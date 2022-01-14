package com.bmts.heating.web.scada.controller.issue;

import com.bmts.heating.commons.entiy.gathersearch.response.issue.BPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.CVModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XSVModeResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "控制读取")
@RestController
@RequestMapping("/read")
public class ReadController {

    @Autowired
    private IssueService issueService;

    @ApiOperation(value = "查询二次补水变频泵数据", response = BPModeResponse.class)
    @GetMapping("/bpMode")
    public Response bpMode(@RequestParam Integer systemId) {
        return issueService.bpMode(systemId);
    }

    @ApiOperation(value = "查询一次调节阀数据", response = CVModeResponse.class)
    @GetMapping("/cvMode")
    public Response cvMode(@RequestParam Integer systemId) {
        return issueService.cvMode(systemId);
    }

    @ApiOperation(value = "查询二次循环泵数据", response = XPModeResponse.class)
    @GetMapping("/xpMode")
    public Response xpMode(@RequestParam Integer systemId) {
        return issueService.xpMode(systemId);
    }

    @ApiOperation(value = "查询泄压阀数据", response = XSVModeResponse.class)
    @GetMapping("/xsvMode")
    public Response xsvMode(@RequestParam Integer systemId) {
        return issueService.xsvMode(systemId);
    }

}
