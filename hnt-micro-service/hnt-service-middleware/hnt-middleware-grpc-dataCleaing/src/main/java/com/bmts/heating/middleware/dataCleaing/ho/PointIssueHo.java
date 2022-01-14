package com.bmts.heating.middleware.dataCleaing.ho;

import com.bmts.heating.commons.container.performance.handler.HistyleCallBackHandler;
import com.bmts.heating.middleware.dataCleaing.pojo.GrpcResult;
import org.springframework.stereotype.Service;

@Service
public class PointIssueHo implements HistyleCallBackHandler {

    public void PointIssueHo() {
        System.out.println("pointIssueHo");
    }

    @Override
    public GrpcResult callWrong(Object[] objects, Throwable e) {
        System.out.println("降级处理...................." + e.getCause());
        return null;
    }
}
