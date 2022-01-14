package com.bmts.heating.monitor.plugins.second.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("big_test")
public class MongoInserDto {

    // id、deviceCode、gropuid、real、time

    @Id
    private String id;
    private String deviceCode;
    private String gropuId;
    private long timeStamp;
    private JSONObject realData;


}
