package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.entity.WebPointSearchUnit;
import com.bmts.heating.commons.basement.model.db.entity.WebPointUnitDetail;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.mapper.PointStandardMapper;
import com.bmts.heating.commons.db.mapper.WebPointSearchUnitMapper;
import com.bmts.heating.commons.db.mapper.WebPointUnitDetailMapper;
import com.bmts.heating.commons.db.service.PointUnitService;
import com.bmts.heating.commons.db.service.WebPointSearchUnitService;
import com.bmts.heating.commons.db.service.WebPointUnitDetailService;
import com.bmts.heating.commons.entiy.baseInfo.request.PointPageConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointUnitDetailDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointTypeSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.SelectPointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointPageConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointUnitDetailResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.SelectPointConfigResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "配置各页面选择点位信息")
@RestController
@RequestMapping("/selectPointConfig")
@Slf4j
public class SelectPointConfigJoggle {
    @Autowired
    private WebPointUnitDetailMapper webPointUnitDetailMapper;
    @Autowired
    private WebPointSearchUnitMapper webPointSearchUnitMapper;
    @Autowired
    private WebPointUnitDetailService webPointUnitDetailService;

    @Autowired
    private WebPointSearchUnitService webPointSearchUnitService;

    @Autowired
    private PointStandardMapper pointStandardMapper;

    @Autowired
    private PointUnitService pointUnitService;

    @ApiOperation("添加页面选择点位信息")
    @PostMapping("/configPoint")
    public Response selectPointConfig(@RequestBody SelectPointConfigDto selectPointConfigDto) {
        try {
            //删除所有包含pagekey的二类标准点数据
            QueryWrapper<WebPointSearchUnit> wrapper = new QueryWrapper<>();
            wrapper.eq("pageKey", selectPointConfigDto.getPageKey());
            List<WebPointSearchUnit> webPointSearchUnitList = webPointSearchUnitService.list(wrapper);
            if (webPointSearchUnitList.stream().count() == 0) {
                return configPointPage(selectPointConfigDto);
            } else {
                List<Integer> ids = webPointSearchUnitList.stream().map(s -> s.getId()).collect(Collectors.toList());
                QueryWrapper<WebPointUnitDetail> webPointUnitDetailQueryWrapper = new QueryWrapper<>();
                webPointUnitDetailQueryWrapper.in("webPointSearchUnitId", ids);
                webPointUnitDetailService.remove(webPointUnitDetailQueryWrapper);
                //删除大类分类数据
                Boolean removePointSearchUnitStatus = webPointSearchUnitService.remove(wrapper);
                if (removePointSearchUnitStatus) {
                    return configPointPage(selectPointConfigDto);
                } else {
                    log.error(new Date() + "清除配置列表大类分类数据失败，删除页面key信息为：" + selectPointConfigDto.getPageKey());
                    return Response.fail();
                }
            }
        } catch (Exception e) {
            log.error(new Date() + "页面配置接口数据更新失败", e.getMessage());
            return Response.fail();
        }
    }

//     public Response configPointPage(SelectPointConfigDto selectPointConfigDto)
//     {
//         try {
//             List<PointPageConfig> pointPageConfigs = selectPointConfigDto.getPointPageConfigs();
//             for (PointPageConfig pointPageConfig : pointPageConfigs) {
//                 //获取大类分类信息
//                 WebPointSearchUnit webPointSearchUnit = new WebPointSearchUnit();
//                 webPointSearchUnit.setPageKey(selectPointConfigDto.getPageKey());
//                 webPointSearchUnit.setPointUnitId(pointPageConfig.getPointUnitId());
//                 webPointSearchUnit.setSort(pointPageConfig.getSort());
//                 Boolean resultStatus = webPointSearchUnitService.save(webPointSearchUnit);
//                 if (resultStatus) {
//                     Integer id = webPointSearchUnit.getId();
//                     for (PointUnitDetailDto pointUnitDetail : pointPageConfig.getPointUnitDetailDtoList()) {
//                         WebPointUnitDetail webPointUnitDetail = new WebPointUnitDetail();
//                         webPointUnitDetail.setPointStandardId(pointUnitDetail.getPointStandardId());
//                         webPointUnitDetail.setSort(pointUnitDetail.getSort());
//                         webPointUnitDetail.setWebPointSearchUnitId(id);
//                         Boolean unitDetailStatus = webPointUnitDetailService.save(webPointUnitDetail);
//                         if (unitDetailStatus) {
//                             continue;
//                         } else {
//                             log.error(new Date() + "页面配置二类标准点数据插入失败，插入信息为：" + webPointUnitDetail);
//                             continue;
//                         }
//                     }
//                     continue;
//                 } else {
//                     log.error(new Date() + "选择大类分类数据插入失败，插入信息为：" + webPointSearchUnit);
//                     continue;
//                 }
//             }
//             return  Response.success();
//         }catch (Exception e){
//             return  Response.fail();
//         }
//     }

    public Response configPointPage(SelectPointConfigDto selectPointConfigDto) {
        try {
            List<WebPointSearchUnit> webPointSearchUnitList = selectPointConfigDto.getPointPageConfigs().stream().filter(s -> s.getPointUnitId() != null).map(m -> {
                WebPointSearchUnit webPointSearchUnit = new WebPointSearchUnit();
                webPointSearchUnit.setPageKey(selectPointConfigDto.getPageKey());
                webPointSearchUnit.setPointUnitId(m.getPointUnitId());
                webPointSearchUnit.setSort(m.getSort());
                return webPointSearchUnit;
            }).collect(Collectors.toList());
            Boolean webPointSearchUnitResult = webPointSearchUnitService.saveBatch(webPointSearchUnitList);
            if (webPointSearchUnitResult) {
                List<WebPointUnitDetail> webPointUnitDetailList = new ArrayList<>();
                for (WebPointSearchUnit w : webPointSearchUnitList) {
                    PointPageConfig pointPageConfig = selectPointConfigDto.getPointPageConfigs().stream().filter(f -> f.getPointUnitId() != null && f.getPointUnitId().equals(w.getPointUnitId())).findFirst().orElse(null);
                    for (PointUnitDetailDto pointUnitDetailDto : pointPageConfig.getPointUnitDetailDtoList()) {
                        WebPointUnitDetail webPointUnitDetail = new WebPointUnitDetail();
                        webPointUnitDetail.setPointStandardId(pointUnitDetailDto.getPointStandardId());
                        webPointUnitDetail.setSort(pointUnitDetailDto.getSort());
                        webPointUnitDetail.setWebPointSearchUnitId(w.getId());
                        webPointUnitDetailList.add(webPointUnitDetail);
                    }
                }
                Boolean webPointUnitDetaiResult = webPointUnitDetailService.saveBatch(webPointUnitDetailList);
                if (webPointUnitDetaiResult) {
                    return Response.success();
                } else {
                    log.error(new Date() + "页面配置二类标准点数据插入失败，插入信息为：" + webPointUnitDetailList);
                    return Response.fail();
                }
            } else {
                log.error(new Date() + "选择大类分类数据插入失败，插入信息为：" + webPointSearchUnitList);
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("获取配置页面数据状态")
    @GetMapping("/pagePointConfigStatus/{pageKey}")
    public Response SearchPointConfigStatus(@PathVariable String pageKey) {
        try {
            QueryWrapper<WebPointSearchUnit> wrapper = new QueryWrapper<>();
            wrapper.eq("wp.pageKey", pageKey);
            List<PointPageConfigResponse> webPointSearchUnitList = webPointSearchUnitMapper.pointPageConfigResponseList(wrapper);
            if (webPointSearchUnitList.stream().count() == 0) {
                return Response.success(null);
            } else {
                List<Integer> ids = webPointSearchUnitList.stream().map(s -> s.getId()).collect(Collectors.toList());
                QueryWrapper<WebPointUnitDetail> webPointUnitDetailQueryWrapper = new QueryWrapper<>();
                webPointUnitDetailQueryWrapper.in("wp.webPointSearchUnitId", ids);
                List<PointUnitDetailResponse> webPointUnitDetailResponseList = webPointUnitDetailMapper.pointUnitDetailResponseList(webPointUnitDetailQueryWrapper);
                List<PointPageConfigResponse> pointPageConfigResponseList = new ArrayList<>();
                for (PointPageConfigResponse webPointSearchUnit : webPointSearchUnitList) {
                    PointPageConfigResponse pointPageConfigResponse = new PointPageConfigResponse();
                    pointPageConfigResponse.setPointUnitId(webPointSearchUnit.getPointUnitId());
                    pointPageConfigResponse.setSort(webPointSearchUnit.getSort());
                    pointPageConfigResponse.setId(webPointSearchUnit.getId());
                    pointPageConfigResponse.setPointUnitName(webPointSearchUnit.getPointUnitName());
                    pointPageConfigResponse.setPointUnitValue(webPointSearchUnit.getPointUnitValue());
                    List<PointUnitDetailResponse> pointUnitDetailResponses = webPointUnitDetailResponseList.stream().filter(s -> s.getWebPointSearchUnitId().equals(webPointSearchUnit.getId())).collect(Collectors.toList());
                    pointPageConfigResponse.setPointUnitDetailResponseList(pointUnitDetailResponses);
                    pointPageConfigResponseList.add(pointPageConfigResponse);
                }
                SelectPointConfigResponse selectPointConfigResponse = new SelectPointConfigResponse();
                selectPointConfigResponse.setPageKey(pageKey);
                selectPointConfigResponse.setPointPageConfigResponses(pointPageConfigResponseList);
                return Response.success(selectPointConfigResponse);
            }
        } catch (Exception e) {
            log.error(new Date() + "获取配置页面数据状态失败", e.getMessage());
            return Response.fail();
        }
    }

    @ApiOperation("点类别查询")
    @PostMapping("/queryPointType")
    public Response queryPointType(@RequestBody PointTypeSearchDto pointTypeSearchDto) {
        try {
            QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
            if (pointTypeSearchDto.getIsAllInfo()) {
                pointTypeSearchDto.setTypeArrayIds(null);
            } else {
                queryWrapper.in("pu.id", pointTypeSearchDto.getTypeArrayIds());
            }
            if (pointTypeSearchDto.getLevel() != null) {
                queryWrapper.eq("ps.level", pointTypeSearchDto.getLevel());
            }
            return Response.success(pointStandardMapper.listPointStandard(queryWrapper));
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @ApiOperation("点位大类查询")
    @GetMapping("/queryType")
    public Response queryType() {
        try {
            List<PointUnit> collect = pointUnitService.list()
                    .stream()
                    .map(x -> {
                        PointUnit pointUnit = new PointUnit();
                        pointUnit.setUnitName(x.getUnitName());
                        pointUnit.setUnitValue(x.getUnitValue());
                        pointUnit.setSort(x.getSort());
                        pointUnit.setId(x.getId());
                        return pointUnit;
                    })
                    .distinct()
                    .collect(Collectors.toList());
            return Response.success(collect);
        } catch (Exception e) {
            return Response.fail();
        }
    }
}
