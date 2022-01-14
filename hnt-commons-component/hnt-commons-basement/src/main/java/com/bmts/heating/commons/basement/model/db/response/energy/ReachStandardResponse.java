package com.bmts.heating.commons.basement.model.db.response.energy;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/4/29 14:15
 **/
@Data
@ApiModel("达标与否及个数响应类")
public class ReachStandardResponse {
    private Integer qualified;
    private Integer count;
}
