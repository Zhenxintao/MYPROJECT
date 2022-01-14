package com.bmts.heating.bussiness.netBalance.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import org.apache.commons.lang3.StringUtils;


public class WrapperSortUtils {
    public static <T> void sortWrapper(QueryWrapper<T> queryWrapper, BaseDto baseDto) {
        if (StringUtils.isNotBlank(baseDto.getSortName())) {
            if (baseDto.isSortAsc())
                queryWrapper.orderByAsc(baseDto.getSortName());
            else
                queryWrapper.orderByDesc(baseDto.getSortName());
        }
    }
}
