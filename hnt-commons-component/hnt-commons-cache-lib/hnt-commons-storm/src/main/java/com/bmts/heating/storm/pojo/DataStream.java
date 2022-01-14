package com.bmts.heating.storm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataStream {

    private int id;
    private String name;
    private String type;
    private String storeType;
    private Map<String,WashPolicy[]> washMap;   //清洗策略
    private String washProcess; //清洗流程

    public DataStream(int id,String name,String type,String storeType,String washProcess){
        this.id=id;
        this.name=name;
        this.type=type;
        this.storeType=storeType;
        this.washProcess=washProcess;
    }
}
