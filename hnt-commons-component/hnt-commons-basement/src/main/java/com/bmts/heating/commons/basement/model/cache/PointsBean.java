package com.bmts.heating.commons.basement.model.cache;

import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.Data;

import java.util.List;

/**
 * @Author: naming
 * @Description:
 * @Date: Create in 2020/9/28 14:30
 * @Modified by
 */
@Data
public class PointsBean {
    private List<PointL> pointLList;
    private int total;
}
