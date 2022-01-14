package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/11 11:12
 **/
@Data
public class PointRankResponse {
    private String pointColumnName;
    private List<RankItem> rankDetails;
}
