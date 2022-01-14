package com.bmts.heating.bussiness.common.joggle;

import com.bmts.heating.bussiness.common.service.BPModeService;
import com.bmts.heating.bussiness.common.service.CVModeService;
import com.bmts.heating.bussiness.common.service.XPModeService;
import com.bmts.heating.bussiness.common.service.XSVModeService;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "控制读取管理")
@RestController
@RequestMapping("/read")
public class ReadJoggle {

    @Autowired
    private BPModeService bpModeService;

    @Autowired
    private CVModeService cvModeService;

    @Autowired
    private XPModeService xpModeService;

    @Autowired
    private XSVModeService xsvModeService;


    @ApiOperation("查询二次补水变频泵数据")
    @GetMapping("/bpMode/{systemId}")
    public Response bpMode(@PathVariable int systemId) {
        if (systemId <= 0) {
            return Response.fail("参数错误！");
        }
        return bpModeService.query(systemId);
    }

    @ApiOperation("查询一次调节阀数据")
    @GetMapping("/cvMode/{systemId}")
    public Response cvMode(@PathVariable int systemId) {
        if (systemId <= 0) {
            return Response.fail("参数错误！");
        }
        return cvModeService.query(systemId);
    }

    @ApiOperation("查询二次循环泵数据")
    @GetMapping("/xpMode/{systemId}")
    public Response xpMode(@PathVariable int systemId) {
        if (systemId <= 0) {
            return Response.fail("参数错误！");
        }
        return xpModeService.query(systemId);
    }

    @ApiOperation("查询泄压阀数据")
    @GetMapping("/xsvMode/{systemId}")
    public Response xsvMode(@PathVariable int systemId) {
        if (systemId <= 0) {
            return Response.fail("参数错误！");
        }
        return xsvModeService.query(systemId);
    }


}
