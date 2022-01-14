package com.bmts.heating.service.task.energy.pojo;

import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/4/28 14:01
 **/
@Data
public class NetSystemRelation {
    Integer targetId;
    Integer systemId;
    String netName;
}
