package com.bmts.heating.grpc.dataCleaning.kafka;

public interface PointKafkaInterface {

    void product(String topicName, Object obj);

    void consume();
}
