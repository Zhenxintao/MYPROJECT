package com.bmts.heating.middleware.pattern;

import com.bmts.heating.commons.container.performance.handler.HistyleCallBackHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PattHo implements HistyleCallBackHandler {

    public void pattHo(){
        System.out.println("pattHo");
    }

    @Override
    public List<Feature> callWrong(Object[] objects, Throwable e) {
        System.out.println("降级处理...................."+e.getCause());
        return null;
    }
}
