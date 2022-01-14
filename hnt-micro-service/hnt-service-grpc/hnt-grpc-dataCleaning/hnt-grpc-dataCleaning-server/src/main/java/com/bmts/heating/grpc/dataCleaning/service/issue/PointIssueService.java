package com.bmts.heating.grpc.dataCleaning.service.issue;

import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.grpc.dataCleaning.enums.SystemType;
import com.bmts.heating.grpc.dataCleaning.utils.DataTypeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointIssueService {


    /**
     * @Method matureToRawPVSS
     * @Description 熟数据 转生数据
     * <p>
     * 利用Grpc 服务进行数据下发时使用
     * @Param [messageIssue]
     */
    public void matureToRawPVSS(PointOuterClass.PointL issuePoint) {
        // PVSS  数据下发清洗
        String value = issuePoint.getValue();
        if (!StringUtils.isEmpty(value)) {
            // 根据 下发命令的清洗策略进行数据清洗操作
            // int[] washDArray = pl.getWashDArray();
            if (!StringUtils.isEmpty(issuePoint.getPointlsSign())) {
                PointOuterClass.PointL.Builder builder = issuePoint.toBuilder();
                builder.setValue("60");
            }
        }
    }

    /**
     * @Method matureToRawJK
     * @Description 熟数据 转生数据
     * <p>
     * 利用Grpc 服务进行数据下发时使用
     * @Param [messageIssue]
     */
    public void matureToRawJK(PointOuterClass.PointL issuePoint) {
        // JK 数据下发清洗
        String value = issuePoint.getValue();
        if (!StringUtils.isEmpty(value)) {
            // 根据 下发命令的清洗策略进行数据清洗操作
            // int[] washDArray = pl.getWashDArray();
            if (StringUtils.isEmpty(issuePoint.getPointlsSign())) {
                String newValue = DataTypeUtil.type(issuePoint.getType(), value, false);
                PointOuterClass.PointL.Builder builder = issuePoint.toBuilder();
                builder.setValue(newValue);
            }
        }

    }

    /**
     * 熟数据 转生数据
     * <p>
     * 原来利用 Kafka 数据下发时使用的
     *
     * @param pointIssue
     * @return
     */
    public Message_Point_Issue matureToRaw(Message_Point_Issue pointIssue) {
        List<PointL> pointLArry = pointIssue.getPointLS();
        List<PointL> newList = new ArrayList<>();
        if (pointLArry.size() > 0) {
            for (PointL pl : pointLArry) {
                String value = pl.getValue();
                if (!StringUtils.isEmpty(value)) {
                    // 根据 下发命令的清洗策略进行数据清洗操作
                    // int[] washDArray = pl.getWashDArray();
                    if (pl.getPointlsSign().contains(SystemType.JK_SYSTEM.getType())) {
                        String newValue = DataTypeUtil.type(pl.getType(), value, false);
                        pl.setValue(newValue);
                    }
                    newList.add(pl);
                }
            }
        }
        if (newList.size() > 0) {
            pointIssue.setPointLS(newList);
            return pointIssue;
        } else {
            return null;
        }

    }


//    public void matureToRawJK(PointOuterClass.PointL issuePoint) {
//        // JK 数据下发清洗
//        List<PointOuterClass.PointL> pointLSList = messageIssue.getPointLSList();
//        if (pointLSList.size() > 0) {
//            for (PointOuterClass.PointL pl : pointLSList) {
//                String value = pl.getValue();
//                if (!StringUtils.isEmpty(value)) {
//                    // 根据 下发命令的清洗策略进行数据清洗操作
//                    // int[] washDArray = pl.getWashDArray();
//                    if (StringUtils.isEmpty(pl.getPointlsSign())) {
//                        String newValue = DataTypeUtil.type(pl.getType(), value, false);
//                        PointOuterClass.PointL.Builder builder = pl.toBuilder();
//                        builder.setValue(newValue);
//                    }
//                }
//            }
//        }
//
//    }
}
