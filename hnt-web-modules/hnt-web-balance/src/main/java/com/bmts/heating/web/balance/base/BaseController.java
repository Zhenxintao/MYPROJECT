package com.bmts.heating.web.balance.base;

import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author naming
 * @description
 * @date 2021/2/2 15:54
 **/
public class BaseController {
    @Autowired
    protected TSCCRestTemplate template;
    protected String baseServer = "bussiness_baseInfomation";
    protected String gathServer="gather_search";
    protected String netBalanceServer="application_netbalance";
}
