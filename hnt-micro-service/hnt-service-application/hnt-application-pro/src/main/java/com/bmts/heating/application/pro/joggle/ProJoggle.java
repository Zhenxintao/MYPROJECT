package com.bmts.heating.application.pro.joggle;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.middleware.pattern.Feature;
import com.bmts.heating.middleware.pattern.PatternSavantGrpcService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("pro")
public class ProJoggle {

    @Autowired
    private PatternSavantGrpcService patternSavantGrpcService;

    private Logger LOGGER = LoggerFactory.getLogger(ProJoggle.class);

    @RequestMapping("/data/{id}")
    public String getProData(@PathVariable("id") int id) {
        LOGGER.info("pro接口被调用.........");
        List<Feature> flist = null;

        try {
            flist = patternSavantGrpcService.ListFeatures(id, id + "-gaoxiang", id + "-qhd");
            if (flist != null) {
                return flist.size() + "-pro-" + "id";
            } else {
                return "wrong1.page";
            }
        } catch (Exception e) {
            return "wrong2.page";
        }
    }

    @RequestMapping("/overall/{id}")
    public Response testOverAll(@PathVariable("id") int id) {
        LOGGER.info("pro接口测试全局异常.........");

        if (id < 10) {
//            throw new StaffPointsException(ResponseCode.PARAM_ERROR);
        }
        int r = id / 0;
        return Response.assemblingResponse(ResponseCode.SUCCESS, id + 100);
    }
}
