package com.bmts.heating.commons.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataPerm {
    private String nodeType;
    private int level;
    private String id;
}
