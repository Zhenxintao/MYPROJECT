package com.bmts.heating.monitor.plugins.second.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("room")
public class MongoRoomDto {

    @Id
    private String id;
    private Object basic;
    private Object devices;


}
