package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.converter.TemplatePointConverter;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;
import com.bmts.heating.commons.basement.model.db.response.template.TemplatePointResponse;
import com.bmts.heating.commons.db.service.TemplatePointService;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplatePointAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplatePointDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Api(tags = "点模板管理")
@RestController
@RequestMapping("/templatePointJoogle")
@Slf4j
public class TemplatePointJoggle {

	@Autowired
	private TemplatePointService templatePointService;
	@Autowired
	private TemplatePointConverter templatePointConverter;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody TemplatePointDto dto) {
		if (dto.getTid() == null) return Response.paramError();
		try {
			Page<TemplatePointResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
			QueryWrapper<TemplatePoint> queryWrapper = new QueryWrapper<>();
			WrapperSortUtils.sortWrapper(queryWrapper, dto);
			queryWrapper.eq("tp.pointTemplateConfigId", dto.getTid());
			if (StringUtils.isNotBlank(dto.getKeyWord()))
				queryWrapper.like("ps.name", dto.getKeyWord());
			if (dto.getType() != null)
				queryWrapper.eq("ps.type", dto.getType());
			if (dto.getPointConfig() != null)
				queryWrapper.eq("ps.pointConfig",dto.getPointConfig());
			queryWrapper.eq("tp.deleteFlag",false);
			return Response.success(templatePointService.pageUnion(page, queryWrapper));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Response.fail();
		}
	}

	@ApiOperation("删除")
	@DeleteMapping("{id}")
	public Response delete(@PathVariable int id) {
		return templatePointService.removeById(id) ? Response.success() : Response.fail("删除失败");
	}

	@ApiOperation("加载未添加标准参量")
	@GetMapping("/none/{id}")
	public Response loadOtherPointStandard(@PathVariable int id) {
		try {
			Page<TemplatePointResponse> page = new Page<>(1, 999999999);
			return Response.success(templatePointService.loadOtherPoint(page, id));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Response.fail();
		}
	}

	@ApiOperation("加载未添加标准参量/带搜索")
	@PostMapping("/none")
	public Response loadOtherPointStandardSearch(@RequestBody TemplateLoadStandardDto dto) {
		if (dto.getTemplateId() == null) return Response.paramError();
		try {
			Page<PointStandard> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
			if (StringUtils.isNotBlank(dto.getKeyWord()))
				page = templatePointService.loadOtherPoint(page, dto.getTemplateId(), dto.getKeyWord());
			else {
				page = templatePointService.loadOtherPoint(page, dto.getTemplateId());
			}
			HashMap<String, Object> result = new HashMap<>();
			result.put("lastCount", page.getTotal() - page.getRecords().size());
			result.put("res", page.getRecords());
			return Response.success(result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Response.fail();
		}
	}

	@ApiOperation("添加模板采集量")
	@PostMapping("/addBatch")
	public Response addPointStandard(@RequestBody TemplatePointAddDto dto) {
		//添加失败数量
		Collection<Integer> collection = new ArrayList<>();
		dto.getTemplatePoints().forEach(e -> {
			e.setPointTemplateConfigId(dto.getTemplateId());
			e.setCreateTime(LocalDateTime.now());
			try {
				templatePointService.save(templatePointConverter.INSTANCE.dtoToPojo(e));
			} catch (Exception error) {
				error.printStackTrace();
				collection.add(e.getPointStandardId());
			}
		});
		return Response.success(collection, dto.getTemplatePoints().size() - collection.size());
	}

	@ApiOperation("删除模板参量")
	@PostMapping("/deleteBatch")
	public Response deletePointStandard(@RequestBody TemplateDeleteDto dto) {
		QueryWrapper<TemplatePoint> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("pointTemplateConfigId", dto.getTemplateId());
		queryWrapper.in("pointStandardId", dto.getTemplateId());
		return templatePointService.remove(queryWrapper) ? Response.success() : Response.fail();
	}

	@ApiOperation("模板详情信息")
	@GetMapping("/{id}")
	public Response info(@PathVariable Integer id) {
		if (id != null)
			return Response.success(templatePointService.getById(id));
		return Response.paramError();
	}

	@ApiOperation("修改")
	@PutMapping()
	public Response update(@RequestBody TemplatePoint entry) {
		entry.setUpdateTime(LocalDateTime.now());
		return templatePointService.updateById(entry) ? Response.success() : Response.fail();
	}
}
