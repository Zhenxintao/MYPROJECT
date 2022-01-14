package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeasonDetail;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.db.service.CommonHeatSeasonDetailService;
import com.bmts.heating.commons.db.service.CommonHeatSeasonService;
import com.bmts.heating.commons.entiy.baseInfo.request.CommonHeatSeasonDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Api(tags = "供暖季管理")
@RestController
@RequestMapping("/commonHeatSeason")
@Slf4j
public class CommonHeatSeasonJoggle {

    @Autowired
    CommonHeatSeasonService commonHeatSeasonService;

    @Autowired
    CommonHeatSeasonDetailService commonHeatSeasonDetailService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody CommonHeatSeason info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        if (commonHeatSeasonService.save(info))
            return Response.success();
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        if (commonHeatSeasonService.removeById(id))
            return Response.success();
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody CommonHeatSeason info) {
        Response response = Response.fail();
        CommonHeatSeason commonHeatSeason = commonHeatSeasonService.getById(info.getId());
        if (commonHeatSeason == null)
            return Response.fail();
        info.setUpdateTime(LocalDateTime.now());
        if (commonHeatSeasonService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        CommonHeatSeason info = commonHeatSeasonService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody CommonHeatSeasonDto dto) {
        Page<CommonHeatSeason> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        QueryWrapper<CommonHeatSeason> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            queryWrapper.like("heatYear", dto.getKeyWord());
        }
        WrapperSortUtils.sortWrapper(queryWrapper, dto);

//        QueryWrapper<CommonHeatSeason> queryWrapper = new QueryWrapper<>();
//        Page<CommonHeatSeason> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        return Response.success(commonHeatSeasonService.page(page, queryWrapper));
    }

    @ApiOperation("供暖季详情查询")
    @GetMapping("/queryDetail")
    public Response queryDetail(@RequestParam int id) {
        try {
            List<CommonHeatSeasonDetail> commonHeatSeasonDetailList = new ArrayList<>();
            if (id == 0) {
                commonHeatSeasonDetailList = commonHeatSeasonDetailService.list();
                return Response.success(commonHeatSeasonDetailList);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("commonHeatSeasonId", id);
                commonHeatSeasonDetailList = commonHeatSeasonDetailService.listByMap(map);
                return Response.success(commonHeatSeasonDetailList);
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("供暖季详情通过Id查询")
    @GetMapping("/queryHeatSeasonDetailById")
    public Response queryHeatSeasonDetailById(@RequestParam int id) {
        try {
           return Response.success(commonHeatSeasonDetailService.getById(id));
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("供暖季详情添加")
    @PostMapping("/insertDetail")
    public Response insertDetail(@RequestBody CommonHeatSeasonDetail com) {
        try {
            Boolean result = commonHeatSeasonDetailService.save(com);
            if (result) {
                return Response.success();
            } else {
                log.error("供暖季详情信息插入失败", com);
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("供暖季详情修改")
    @PostMapping("/updateDetail")
    public Response updateDetail(@RequestBody CommonHeatSeasonDetail com) {
        try {
            Boolean result = commonHeatSeasonDetailService.updateById(com);
            if (result) {
                return Response.success();
            } else {
                log.error("供暖季详情信息修改失败", com);
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("供暖季详情删除")
    @GetMapping("/deleteDetail")
    public Response deleteDetail(@RequestParam int id) {
        try {
            Boolean result = commonHeatSeasonDetailService.removeById(id);
            if (result) {
                return Response.success();
            } else {
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("根据主供暖季删除")
    @GetMapping("/deleteAllDetail")
    public Response deleteAllDetail(@RequestParam int id) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("commonHeatSeasonId", id);
            Boolean result = commonHeatSeasonDetailService.removeByMap(map);
            if (result) {
                return Response.success();
            } else {
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询供暖季至今天数")
    @GetMapping("/heatSeasonDayNumber")
    public Response heatSeasonDayNumber() {
        QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
        CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
        if (com != null) {
            if (LocalDateTime.now().isBefore(com.getHeatStartTime())) {
                return Response.success(0);
            } else {
                int dayNumber = dateDiff(com.getHeatStartTime(),LocalDateTime.now());
                return Response.success(dayNumber);
            }

        } else {
            return Response.fail("未设置当前时间区间内供暖季信息");
        }
    }

    public static int dateDiff(LocalDateTime dt1, LocalDateTime dt2) {
        //获取第一个时间点的时间戳对应的秒数
        long t1 = dt1.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第一个时间点在是1970年1月1日后的第几天
        long day1 = t1 / (60 * 60 * 24);
        //获取第二个时间点的时间戳对应的秒数
        long t2 = dt2.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第二个时间点在是1970年1月1日后的第几天
        long day2 = t2 / (60 * 60 * 24);
        //返回两个时间点的天数差
        return (int) (day2 - day1);
    }
}
