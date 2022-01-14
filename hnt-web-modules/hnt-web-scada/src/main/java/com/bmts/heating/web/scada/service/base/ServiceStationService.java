package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestParam;

public interface ServiceStationService {
    Response queryServiceStationInfo(String id);
}
