package com.bmts.heating.commons.entiy.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/2/3 16:57
 **/
@Data
public class ResponsePage<T> implements Serializable {
    private int current;
    private int pages;
    private int size;
    private int total;
    private List<T> records;
}
