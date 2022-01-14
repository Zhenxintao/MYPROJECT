package com.bmts.heating.grpc.dataCleaning.service.strategy.pojo;

import lombok.Data;

/**
 * @ClassName: RangeAlarmBO
 * @Description: 扩展字段实体类
 * @Author: pxf
 * @Date: 2020/8/12 10:31
 * @Version: 1.0
 */

@Data
public class RangeAlarmBO {

    // {"alarm":{"h":110,"hh":120,"l":50,"ll":0},"range":{"h":120,"l":0},"washpolicy":"1,2,3"}

    /**
     * 警报
     */
    private Alarm alarm;

    /**
     * 量程
     */
    private Range range;


    @Data
    public class Alarm {
        /**
         * 高线
         */
        private Integer h;
        /**
         * 高高线
         */
        private Integer hh;
        /**
         * 低线
         */
        private Integer l;
        /**
         * 低低线
         */
        private Integer ll;
    }

    @Data
    public class Range {
        /**
         * 高线
         */
        private Integer h;
        /**
         * 低线
         */
        private Integer l;
    }
}
