package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/1/11 11:16
 **/
@Data
public class RankItem {
    private int systemId;
    private String stationName;
    private double value;
    private String systemName;
}
