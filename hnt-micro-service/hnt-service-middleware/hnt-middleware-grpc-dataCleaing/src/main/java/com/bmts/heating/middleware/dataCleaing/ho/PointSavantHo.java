package com.bmts.heating.middleware.dataCleaing.ho;

import com.bmts.heating.commons.container.performance.handler.HistyleCallBackHandler;
import org.springframework.stereotype.Service;

@Service
public class PointSavantHo implements HistyleCallBackHandler {

    public void PointSavantHo() {
        System.out.println("pointSavantHo");
    }

    @Override
    public Boolean callWrong(Object[] objects, Throwable e) {
        System.out.println("降级处理...................." + e.getCause());
        return null;
    }
}
