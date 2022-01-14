package com.bmts.heating.grpc.dataCleaning.service.strategy.impl;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.msmq.PointType;
import com.bmts.heating.grpc.dataCleaning.annotation.Clean;
import com.bmts.heating.grpc.dataCleaning.enums.DataCleanType;
import com.bmts.heating.grpc.dataCleaning.service.strategy.DataCleanStrategy;
import com.bmts.heating.grpc.dataCleaning.utils.ByteOrderUtil;
import com.bmts.heating.grpc.dataCleaning.utils.DataTypeUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 字节序清洗策略2
 */
@Component
@Clean(cleanType = DataCleanType.BYTE_ORDER_BIG_TO_SWAP)
public class JKByteOrderCleanSecond implements DataCleanStrategy<PointL> {

    @Override
    public PointL dataClean(PointL pointL) {
        String oldStr = pointL.getOldValue();
        if (!StringUtils.isEmpty(oldStr) && !Objects.equals("\"\"", oldStr)) {
            // ABCD    转为   BADC
            // BYTE_ORDER_BIG_TO_SWAP(2, "字节序排序"),
            String newStr = ByteOrderUtil.bigToSwap(oldStr);
            String newValue = DataTypeUtil.type(pointL.getType(), newStr, true);
            int newValueLeng = newValue.length();
            // 16 的二进制码  从右往左取值
            if (pointL.getType() == PointType.POINT_BOOL.getType() && newValueLeng == 16) {
//                String address = pointL.getPointAddress();
//                int indexOf = address.indexOf(".");
//                if (indexOf > -1) {
//                    int index = address.charAt(indexOf + 1) - '0';
//                    newValue = String.valueOf(newValue.charAt(newValueLeng - index - 1) - '0');
//                }
            }
            pointL.setValue(newValue);

        }

        return pointL;
    }
}
