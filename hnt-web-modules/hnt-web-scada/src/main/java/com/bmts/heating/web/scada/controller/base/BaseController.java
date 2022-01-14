package com.bmts.heating.web.scada.controller.base;

import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author naming
 * @date 2020/12/30 14:38
 **/
public class BaseController {
    @Autowired
    protected TSCCRestTemplate template;
    protected String baseServer = "bussiness_baseInfomation";
    protected String gathServer="gather_search";
}
