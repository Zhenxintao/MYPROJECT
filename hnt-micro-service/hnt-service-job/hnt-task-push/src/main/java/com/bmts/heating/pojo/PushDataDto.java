package com.bmts.heating.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PushDataDto {
    String siteCode;
    List<PushPointDto> varDatas=new ArrayList<>();
}
