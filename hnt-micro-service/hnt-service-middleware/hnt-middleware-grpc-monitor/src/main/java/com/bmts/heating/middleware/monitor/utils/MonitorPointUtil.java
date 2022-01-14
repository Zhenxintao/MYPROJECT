package com.bmts.heating.middleware.monitor.utils;

import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.PointL;
import org.springframework.util.StringUtils;

import java.util.List;

public class MonitorPointUtil {

    public static void toProto(PointL point, PointOuterClass.PointL.Builder builder) {
        int pointId = point.getPointId();
        String pointName = point.getPointName();
//        String pointAddress = point.getPointAddress();
        int type = point.getType();
        String deviceId = point.getDeviceId();
        String pointlsSign = point.getPointlsSign();
        String oldValue = point.getOldValue();
        String value = point.getValue();
        Long timeStrap = point.getTimeStrap();
        int qualityStrap = point.getQualityStrap();
        int level = point.getLevel();
        int relevanceId = point.getRelevanceId();
        int[] washArray = point.getWashArray();
        int dataType = point.getDataType();
        String expandDesc = point.getExpandDesc();
        int[] washDArray = point.getWashDArray();
        String applicationName = point.getApplicationName();
        //        String units = point.getUnits();
        //        String showType = point.getShowType();
        if (!StringUtils.isEmpty(pointId)) {
            builder.setPointId(pointId);
        }
        if (!StringUtils.isEmpty(pointName)) {
            builder.setPointName(pointName);
        }
//        if (!StringUtils.isEmpty(pointAddress)) {
//            builder.setPointAddress(pointAddress);
//        }
        if (!StringUtils.isEmpty(type)) {
            builder.setType(type);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            builder.setDeviceId(deviceId);
        }
        if (!StringUtils.isEmpty(pointlsSign)) {
            builder.setPointlsSign(pointlsSign);
        }
        if (!StringUtils.isEmpty(oldValue)) {
            builder.setOldValue(oldValue);
        }
        if (!StringUtils.isEmpty(value)) {
            builder.setValue(value);
        }
        if (!StringUtils.isEmpty(timeStrap)) {
            builder.setTimeStrap(timeStrap);
        }
        if (!StringUtils.isEmpty(qualityStrap)) {
            builder.setQualityStrap(qualityStrap);
        }
        if (level > 0) {
            builder.setLevel(level);
        }
        if (relevanceId > 0) {
            builder.setRelevanceId(relevanceId);
        }
        if (washArray != null && washArray.length > 0) {
            for (int i = 0; i < washArray.length; i++) {
                builder.addWashArray(washArray[i]);
            }
        }
//        if (!StringUtils.isEmpty(units)) {
//            builder.setUnits(units);
//        }
//        if (!StringUtils.isEmpty(showType)) {
//            builder.setShowType(showType);
//        }
        if (!StringUtils.isEmpty(dataType)) {
            builder.setDataType(dataType);
        }
        if (!StringUtils.isEmpty(expandDesc)) {
            builder.setExpandDesc(expandDesc);
        }
        if (washDArray != null && washDArray.length > 0) {
            for (int i = 0; i < washDArray.length; i++) {
                builder.addWashDArray(washDArray[i]);
            }
        }
        if (!StringUtils.isEmpty(applicationName)) {
            builder.setApplicationName(applicationName);
        }
    }


    public static PointL toPojo(PointOuterClass.PointL po) {
        PointL point = new PointL();
        int pointId = po.getPointId();
        String pointName = po.getPointName();
//        String pointAddress = po.getPointAddress();
        int type = po.getType();
        String deviceId = po.getDeviceId();
        String pointlsSign = po.getPointlsSign();
        String oldValue = po.getOldValue();
        String value = po.getValue();
        Long timeStrap = po.getTimeStrap();
        int qualityStrap = po.getQualityStrap();
        int level = po.getLevel();
        int relevanceId = po.getRelevanceId();
        List<Integer> washArrayList = po.getWashArrayList();
        int dataType = po.getDataType();
        String expandDesc = po.getExpandDesc();
        List<Integer> washDArrayList = po.getWashDArrayList();
        String applicationName = po.getApplicationName();
//        String units = po.getUnits();
//        String showType = po.getShowType();
        if (!StringUtils.isEmpty(pointId)) {
            point.setPointId(pointId);
        }
        if (!StringUtils.isEmpty(pointName)) {
            point.setPointName(pointName);
        }
//        if (!StringUtils.isEmpty(pointAddress)) {
//            point.setPointAddress(pointAddress);
//        }
        if (!StringUtils.isEmpty(type)) {
            point.setType(type);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            point.setDeviceId(deviceId);
        }
        if (!StringUtils.isEmpty(pointlsSign)) {
            point.setPointlsSign(pointlsSign);
        }
        if (!StringUtils.isEmpty(oldValue)) {
            point.setOldValue(oldValue);
        }
        if (!StringUtils.isEmpty(value)) {
            point.setValue(value);
        }
        if (!StringUtils.isEmpty(timeStrap)) {
            point.setTimeStrap(timeStrap);
        }
        if (!StringUtils.isEmpty(qualityStrap)) {
            point.setQualityStrap(qualityStrap);
        }
        if (level > 0) {
            point.setLevel(level);
        }
        if (relevanceId > 0) {
            point.setRelevanceId(relevanceId);
        }
        if (washArrayList != null && washArrayList.size() > 0) {
            int[] arr = new int[washArrayList.size()];
            for (int i = 0; i < washArrayList.size(); i++) {
                arr[i] = washArrayList.get(i);
            }
            point.setWashArray(arr);
        }
//        if (!StringUtils.isEmpty(units)) {
//            point.setUnits(units);
//        }
//        if (!StringUtils.isEmpty(showType)) {
//            point.setShowType(showType);
//        }
        if (!StringUtils.isEmpty(dataType)) {
            point.setDataType(dataType);
        }
        if (!StringUtils.isEmpty(expandDesc)) {
            point.setExpandDesc(expandDesc);
        }
        if (washDArrayList != null && washDArrayList.size() > 0) {
            int[] arr = new int[washDArrayList.size()];
            for (int i = 0; i < washDArrayList.size(); i++) {
                arr[i] = washDArrayList.get(i);
            }
            point.setWashDArray(arr);
        }
        if (!StringUtils.isEmpty(applicationName)) {
            point.setApplicationName(applicationName);
        }
        return point;
    }

}
