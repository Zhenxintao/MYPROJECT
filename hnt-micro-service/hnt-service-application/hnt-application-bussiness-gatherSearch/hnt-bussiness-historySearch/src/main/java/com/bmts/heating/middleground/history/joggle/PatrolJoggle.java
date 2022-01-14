package com.bmts.heating.middleground.history.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.db.service.CommonHeatSeasonService;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "历史数据管理")
@Slf4j
@RestController
@RequestMapping("/patrol")
public class PatrolJoggle {

	@Autowired
	private CommonHeatSeasonService commonHeatSeasonService;

	@GetMapping("/stat")
	public Response patrolStat(){
		LocalDateTime now = LocalDateTime.now();
		QueryWrapper<CommonHeatSeason> wrapper = new QueryWrapper<>();
		wrapper.gt("heatStartTime",now);
		wrapper.lt("heatEndTime",now);
		//获取供暖季数据
		List<CommonHeatSeason> list = commonHeatSeasonService.list(wrapper);
		if(CollectionUtils.isEmpty(list)){
			return Response.success(list.get(0));
		}
		return Response.fail();
	}
}
