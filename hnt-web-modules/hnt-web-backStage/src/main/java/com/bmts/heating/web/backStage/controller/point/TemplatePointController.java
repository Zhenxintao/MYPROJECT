package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.template.TemplatePointResponse;
import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplatePoint;
import com.bmts.heating.commons.entiy.baseInfo.request.template.*;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.TemplatePointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Api(tags = "模板点管理")
@RestController
@RequestMapping("/templatePointJoogle")
@Slf4j
public class TemplatePointController {

	@Autowired
	private TemplatePointService templatePointService;

	@ApiOperation(value = "表格",response = TemplatePointResponse[].class)
	@PostMapping("/page")
	public Response page(@RequestBody TemplatePointDto dto) {
		return templatePointService.page(dto);
	}

	@ApiOperation(value = "加载未添加标准参量",response = TemplatePointResponse[].class)
	@GetMapping("/none/{id}")
	public Response loadOtherPointStandard(@PathVariable int id) {
		return templatePointService.loadOtherPointStandard(id);
	}

	@ApiOperation(value = "加载未添加标准参量/带搜索",response = PointStandard[].class)
	@PostMapping("/none")
	public Response loadOtherPointStandardSearch(@RequestBody TemplateLoadStandardDto dto) {
		return templatePointService.loadOtherPointStandardSearch(dto);
	}

	@ApiOperation(value = "添加控制量模板参量",response = Integer[].class)
	@PostMapping("/addBatch")
	public Response addPointStandard(@RequestBody TemplatePointAddDto dto, HttpServletRequest request) {
		Integer userId = null;
		Integer jwtUserID = JwtUtils.getUserId(request);
		if (jwtUserID != null) {
			userId = jwtUserID;
		}
		String userName = null;
		String jwtUserName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(jwtUserName)) {
			userName = jwtUserName;
		}
		Collection<TemplatePoint> templatePoints = dto.getTemplatePoints();
		if (!CollectionUtils.isEmpty(templatePoints)) {
			for (TemplatePoint template : templatePoints) {
				template.setUserId(userId);
				template.setCreateUser(userName);
			}
		}
		return templatePointService.addPointStandard(dto);
	}

	@ApiOperation(value = "删除模板参量")
	@PostMapping("/deleteBatch")
	public Response deletePointStandard(@RequestBody TemplateDeleteDto dto) {
		return templatePointService.deletePointStandard(dto);
	}

	@ApiOperation(value = "模板详情信息",response = TemplatePoint.class)
	@GetMapping("/{id}")
	public Response info(@PathVariable String id) {
		return templatePointService.info(id);
	}

	@ApiOperation(value = "修改")
	@PutMapping()
	public Response update(@RequestBody TemplatePoint entry, HttpServletRequest request) {
		String userName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(userName)) {
			entry.setUpdateUser(userName);
		}
		return templatePointService.update(entry);
	}

	@ApiOperation(value = "删除")
	@DeleteMapping("{id}")
	public Response delete(@PathVariable int id) {
		return templatePointService.delete(id);
	}
}
