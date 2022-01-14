package com.bmts.heating.web.scada.controller.issue;

import com.bmts.heating.commons.basement.model.db.entity.LogOperationControl;
import com.bmts.heating.commons.entiy.baseInfo.request.LogOperationDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "控制下发日志管理")
@RestController
@RequestMapping("/logOperation")
public class LogOperationController {


    @Autowired
    private TSCCRestTemplate template;

    private String gathServer = "gather_search";

    @ApiOperation(value = "查询控制下发日志", response = LogOperationControl.class)
    @PostMapping("/page")
    public Response logOperation(@RequestBody LogOperationDto dto) {
        return template.doHttp("/logOperation/page", dto, gathServer, Response.class, HttpMethod.POST);
    }

}
