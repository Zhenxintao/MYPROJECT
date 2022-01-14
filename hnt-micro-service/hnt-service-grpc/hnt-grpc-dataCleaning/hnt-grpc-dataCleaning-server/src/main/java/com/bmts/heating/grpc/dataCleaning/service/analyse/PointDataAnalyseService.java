package com.bmts.heating.grpc.dataCleaning.service.analyse;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.grpc.dataCleaning.enums.SystemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PointDataAnalyseService {


    @Autowired
    private JKPointCleanService jkPointCleanService;

    @Autowired
    private PVSSPointCleanService pvssPointCleanService;


    static Integer number = 0;

    /**
     * 原生数据 解析  根据策略进行清洗
     *
     * @param list
     * @return
     */
    public boolean pointAnalyse(List<PointL> list, String systemType) {
        number += list.size();
        boolean status = false;
        long statTime = System.currentTimeMillis();
        if (Objects.equals(systemType, SystemType.JK_SYSTEM.getType())) {
            status = jkPointCleanService.jkClean(list);
        }
        if (Objects.equals(systemType, SystemType.PVSS_SYSTEM.getType())) {
            status = pvssPointCleanService.pvssClean(list);
        }
        System.out.println("统计 " + list.size() + " 条数据，清洗耗时：" + (System.currentTimeMillis() - statTime));

        System.out.println("统计-----" + number + "--------");
        return status;
    }


}
