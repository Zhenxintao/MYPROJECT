package com.bmts.heating.service.task.storm.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
public class SecondMongoDto {
    @Id
    private String id;
    @Field("devices")
    private Map<String, Map> devices;
}
