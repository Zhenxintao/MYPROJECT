package com.bmts.heating.pojo;

import lombok.Data;

@Data
public class PushPointDto {
    String varCode;
    Float value;
    Integer dataType = 5;
}
