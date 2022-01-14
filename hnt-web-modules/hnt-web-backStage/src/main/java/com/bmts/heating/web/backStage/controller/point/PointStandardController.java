package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.request.PointStandardAddDto;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardFullResponse;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointStandardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "标准点表管理")
@RestController
@RequestMapping("/pointStandard")
public class PointStandardController {

    @Autowired
    private PointStandardService pointStandardService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody PointStandardAddDto info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.getPointStandard().setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.getPointStandard().setCreateUser(userName);
        }
        return pointStandardService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return pointStandardService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody PointStandardAddDto info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.getPointStandard().setUpdateUser(userName);
        }
        return pointStandardService.update(info);
    }

    @ApiOperation(value = "详情",response = PointStandard.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return pointStandardService.detail(id);
    }

    @ApiOperation(value = "查询",response = PointStandardResponse[].class)
    @PostMapping("/query")
    public Response query(@RequestBody PointStandardDto dto) {
        return pointStandardService.query(dto);
    }

    @ApiOperation(value = "标准点表导入")
    @PostMapping("/import")
    public Response importExcel(@RequestParam MultipartFile file, @RequestParam Integer pointConfig, HttpServletRequest request) {
        PointStandardImportDto dto = new PointStandardImportDto();
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            dto.setUserName(userName);
        }
        dto.setPointConfig(pointConfig);
        return pointStandardService.importExcel(file, dto);
    }

    @ApiOperation(value = "查询",response = PointStandardResponse[].class)
    @PostMapping("/list")
    public Response list(@RequestBody PointStandardDto dto) {
        return pointStandardService.list(dto);
    }

    @ApiOperation(value = "全量点位查询",response = PointStandardFullResponse[].class)
    @GetMapping("/pointStandardFullList")
    public  Response pointStandardFullList()
    {
        return pointStandardService.pointStandardFullList();
    }

    @ApiOperation(value = "查询配置点表数据",response = PointStandardFullResponse[].class)
    @GetMapping("/pointStandardList")
    public  Response pointStandardList()
    {
        return pointStandardService.pointStandardList();
    }

    @ApiOperation(value = "添加项目使用点表信息")
    @PostMapping("/insertPointStandard")
    public  Response insertPointStandard(@RequestBody PointStandardChangeDto pointStandardChangeDto)
    {
        return pointStandardService.insertPointStandard(pointStandardChangeDto);
    }

    @ApiOperation(value = "删除项目使用点表信息")
    @PostMapping("/deletePointStandard")
    public Response deletePointStandard(@RequestBody PointStandardChangeDto pointStandardChangeDto)
    {
        return pointStandardService.deletePointStandard(pointStandardChangeDto);
    }

    @ApiOperation(value = "站/源 初始化/删除 历史库")
    @GetMapping("/initialTd")
    public Response initialTdPointStandard(@RequestParam int level){
        return pointStandardService.initialTdPointStandard(level);
    }

    @ApiOperation(value = "修改当前标准点是否同步到td库状态")
    @GetMapping("/updateById")
    public Response updatePointStandardTd(@RequestParam int id){
        return pointStandardService.updatePointStandardTd(id);
    }
}
