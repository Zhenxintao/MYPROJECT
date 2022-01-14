package com.bmts.heating.commons.basement.model.db.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/19 13:57
 **/
@ApiModel("批量删除实体")
@Data
public class BatchDelete {
    List<Integer> ids;
    Integer type;
}
