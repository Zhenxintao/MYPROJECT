package com.bmts.heating.bussiness.common.common;

import lombok.Data;

/**
 * @Author: naming
 * @Description: 分页基础类
 * @Date: Create in 2020/9/27 18:16
 * @Modified by
 */
@Data
public class PageBase {
    private int currentPage = 1;
    private int pageCount = 20;
}
