package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.gathersearch.request.CurveDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface CurveService {

    Response curve(CurveDto param);

    Response dataCurve(CurveDto param);

    Response dayCurveContrast(CurveDto param);

    Response energyCurve(CurveDto param);
}
