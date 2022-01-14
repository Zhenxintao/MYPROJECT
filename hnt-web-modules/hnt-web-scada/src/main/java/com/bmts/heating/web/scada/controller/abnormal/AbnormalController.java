package com.bmts.heating.web.scada.controller.abnormal;

import com.bmts.heating.commons.entiy.baseInfo.request.AbnormalDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "异常数据管理")
@RestController
@RequestMapping("/abnormal")
public class AbnormalController extends BaseController {

    @ApiOperation("查询异常数据")
    @PostMapping("/page")
    public Response pageTd(@RequestBody AbnormalDto dto) {
        return template.doHttp("/abnormal/page", dto, gathServer, Response.class, HttpMethod.POST);
    }
}
