package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.WebScreenConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Api(tags = "界面显示配置")
@RestController
@RequestMapping("/screen")
public class WebScreenConfigJoggle {

	@Autowired
	WebScreenConfigService webScreenConfigService;

	@ApiOperation("查询")
	@GetMapping("/query")
	public Response queryWebScreenConfig() throws MicroException {
		Response response = Response.fail();
		try{
			List<WebScreenConfig> list = webScreenConfigService.list();
			if (CollectionUtils.isEmpty(list)){
				return Response.fail("未查询到数据");
			}
			return Response.success(list);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return response;
		}

	}

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response queryWebScreenConfigPage(@RequestBody WebScreenConfigDto webScreenConfigDto){
		Response response = Response.fail();
		QueryWrapper queryWrapper = new QueryWrapper();
		if (StringUtils.isNotBlank(webScreenConfigDto.getKeyWord())){
			queryWrapper.like("name",webScreenConfigDto.getKeyWord());
		}
		Page<WebScreenConfig> page = webScreenConfigService.page(new Page<>(webScreenConfigDto.getCurrentPage(), webScreenConfigDto.getPageCount()),queryWrapper);
		return Response.success(page);
	}

	@ApiOperation("新增")
	@PostMapping("/insert")
	public Response insertWebScreenConfig(@RequestBody WebScreenConfig webScreenConfig){
		Response response = Response.fail();
		webScreenConfig.setCreateDate(LocalDateTime.now());
		WebScreenConfig one = webScreenConfigService.getOne(Wrappers.<WebScreenConfig>lambdaQuery().eq(WebScreenConfig::getName, webScreenConfig.getName()));
		if (one !=null ){
			return Response.fail("该名称已存在！");
		}
		webScreenConfig.setPageKey(IdWorker.getIdStr());
		boolean flag = webScreenConfigService.save(webScreenConfig);
		if (flag){
			return Response.success();
		}
		return response;
	}

	@ApiOperation("修改")
	@PutMapping("/update")
	public Response updateWebScreenConfig(@RequestBody WebScreenConfig webScreenConfig){
		Response response = Response.fail();
		WebScreenConfig one = webScreenConfigService.getById(webScreenConfig.getId());
		if (one == null){
			return Response.fail("该配置不存在");
		}
		webScreenConfig.setPageKey(one.getPageKey());
		boolean flag = webScreenConfigService.updateById(webScreenConfig);
		if (flag){
			return Response.success();
		}
		return response;
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete")
	public Response deleteWebScreenConfig(@RequestParam("id") int id){
		Response response = Response.fail();
		WebScreenConfig one = webScreenConfigService.getById(id);
		if (one == null){
			return Response.fail("该配置不存在");
		}
		boolean flag = webScreenConfigService.removeById(id);
		if (flag){
			return Response.success();
		}
		return response;
	}

	@ApiOperation("查询详情")
	@GetMapping("/detail")
	public Response queryDetail(@RequestParam("id")int id){
		Response response = Response.fail();
		WebScreenConfig one = webScreenConfigService.getById(id);
		if (one == null){
			return Response.fail("该用户不存在");
		}
		return Response.success(one);
	}

	@ApiOperation("查询页面唯一标识")
	@PostMapping("/queryPageKey")
	public Response queryPageKey(@RequestBody WebScreenConfig webScreenConfig){
		Response response = Response.fail();
		WebScreenConfig one = webScreenConfigService.getOne(Wrappers.<WebScreenConfig>lambdaQuery().eq(WebScreenConfig::getEventName, webScreenConfig.getEventName()).eq(WebScreenConfig::getAction, webScreenConfig.getAction()).eq(WebScreenConfig::getPageIndex, webScreenConfig.getPageIndex()));
		if (one == null){
			return Response.fail("不存在");
		}
		return Response.success(one);
	}
}
