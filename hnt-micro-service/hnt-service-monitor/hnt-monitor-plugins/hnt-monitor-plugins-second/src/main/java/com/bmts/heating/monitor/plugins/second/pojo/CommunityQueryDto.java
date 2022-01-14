package com.bmts.heating.monitor.plugins.second.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
//@Document("community")
public class CommunityQueryDto {
    @Id
    private String id;
    @Field("devices")
    private Map<String,Map> devices;
}
