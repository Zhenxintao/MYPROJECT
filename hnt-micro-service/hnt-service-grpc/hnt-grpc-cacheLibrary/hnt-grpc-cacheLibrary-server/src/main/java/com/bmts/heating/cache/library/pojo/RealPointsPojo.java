package com.bmts.heating.cache.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author naming
 * @description
 * @date 2021/1/31 16:03
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealPointsPojo {
    int level;
    int relevanceId;
    String[] pointNames;
}
