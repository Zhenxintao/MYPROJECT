package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.entiy.baseInfo.request.DicDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "字典管理")
@RestController
@RequestMapping("/dic")
public class DicController {

	@Autowired
	private DicService dicService;

	@ApiOperation(value = "新增")
	@PostMapping
	public Response insert(@RequestBody Dic info, HttpServletRequest request) {
		Integer userId = JwtUtils.getUserId(request);
		if (userId != null) {
			info.setUserId(userId);
		}
		String userName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(userName)) {
			info.setCreateUser(userName);
		}
		return dicService.insert(info);
	}

	@ApiOperation(value = "删除")
	@DeleteMapping
	public Response delete(@RequestParam int id) {
		return dicService.delete(id);
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public Response update(@RequestBody Dic info, HttpServletRequest request) {
		String userName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(userName)) {
			info.setUpdateUser(userName);
		}
		return dicService.update(info);
	}

	@ApiOperation(value = "详情", response = Dic.class)
	@GetMapping
	public Response detail(@RequestParam int id) {
		return dicService.detail(id);
	}

	@ApiOperation(value = "查询", response = Dic[].class)
	@PostMapping("/query")
	public Response query(@RequestBody DicDto dto) {
		return dicService.query(dto);
	}

	@ApiOperation(value = "查询全部", response = Dic[].class)
	@PostMapping("/queryAll")
	public Response queryAll() {
		return dicService.queryAll();
	}
}
