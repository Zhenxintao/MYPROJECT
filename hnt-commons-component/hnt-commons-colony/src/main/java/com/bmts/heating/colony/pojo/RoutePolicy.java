package com.bmts.heating.colony.pojo;

import lombok.Data;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

@Data
@Component
@Description("路由策略")
public class RoutePolicy {

    private int type;       //集群类型(kafka,redis,el)
    private String dataSet;     //设备编号

    public static class Type{
        public static final int KAFKA = 1 ;
        public static final int REDIS = 2 ;
        public static final int EL = 3 ;
    }
}
