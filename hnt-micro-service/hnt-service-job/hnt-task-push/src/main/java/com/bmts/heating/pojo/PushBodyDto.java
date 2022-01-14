package com.bmts.heating.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PushBodyDto {
    String userID = "relijituan";
    String password = "E10ADC3949BA59ABBE56E057F20F883E";
    List<PushDataDto> data = new ArrayList<>();
}
