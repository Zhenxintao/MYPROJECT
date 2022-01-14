package com.bmts.heating.signalr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmResponse {
    private String alarmTitle;
    private LocalDateTime time;
    private String position;
    private String alarmType;
    private int level;
    private String classify;
    private double value;
    private String alarmDes;
    private String station;
    private String system;
}
