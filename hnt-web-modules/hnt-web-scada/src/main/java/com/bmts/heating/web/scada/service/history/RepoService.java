package com.bmts.heating.web.scada.service.history;

import com.bmts.heating.commons.entiy.gathersearch.request.ProductReportDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleStationRepoDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface RepoService {

	Response table(SingleStationRepoDto param);

	Response productReport(ProductReportDto param);
}
