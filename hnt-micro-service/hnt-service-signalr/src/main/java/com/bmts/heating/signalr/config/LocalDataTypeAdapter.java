package com.bmts.heating.signalr.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author naming
 * @date 2021/1/6 11:57
 **/
public class LocalDataTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(jsonElement instanceof JsonPrimitive)){
            throw new JsonParseException("The date should be a string value");
        }
        return null;
    }

    @Override
    public JsonElement serialize(LocalDateTime localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String createTime = dateTimeFormatter.format(localDate);
        return new JsonPrimitive(createTime);
    }
}
