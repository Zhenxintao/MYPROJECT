package com.bmts.heating.bussiness.common.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationControl;
import com.bmts.heating.commons.db.service.LogOperationControlService;
import com.bmts.heating.commons.entiy.baseInfo.request.LogOperationDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "控制下发日志管理")
@RestController
@RequestMapping("/logOperation")
public class LogOperationControlJoggle {

    @Autowired
    private LogOperationControlService logOperationControlService;


    @ApiOperation("查询控制下发日志")
    @PostMapping("/page")
    public Response logOperation(@RequestBody LogOperationDto dto) {
        QueryWrapper<LogOperationControl> queryWrapper = new QueryWrapper<>();
        Page<LogOperationControl> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        if (StringUtils.isNotBlank(dto.getStartTime())) {
            queryWrapper.ge("operationTime", dto.getStartTime());
        }
        if (StringUtils.isNotBlank(dto.getEndTime())) {
            queryWrapper.le("operationTime", dto.getEndTime());
        }
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper.like("company", dto.getKeyWord()).or().like("stationName", dto.getKeyWord()));
        }
        if (StringUtils.isNotBlank(dto.getSortName())) {
            if (dto.isSortAsc()) {
                queryWrapper.orderByAsc(dto.getSortName());
            } else {
                queryWrapper.orderByDesc(dto.getSortName());
            }
        }
        Page<LogOperationControl> pages = logOperationControlService.page(page, queryWrapper);
        return Response.success(pages);
    }

}
