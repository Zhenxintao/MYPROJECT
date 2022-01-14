package com.bmts.heating.web.scada.controller.history;

import com.bmts.heating.commons.entiy.gathersearch.request.ProductReportDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleStationRepoDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.history.RepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "历史报表")
@RestController
@RequestMapping("/historySearch")
public class RepoController {

	@Autowired
	private RepoService repoService;

	@ApiOperation("单站查询历史报表")
	@PostMapping("/table")
	public Response table(@RequestBody SingleStationRepoDto param) {
		return repoService.table(param);
	}

	@ApiOperation("生产调度报表")
	@PostMapping("/report/table")
	public Response productReport(@RequestBody ProductReportDto param) {
		return repoService.productReport(param);
	}

}
