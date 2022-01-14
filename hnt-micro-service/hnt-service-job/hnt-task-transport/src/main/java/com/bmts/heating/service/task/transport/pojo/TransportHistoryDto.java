package com.bmts.heating.service.task.transport.pojo;

import com.bmts.heating.commons.entiy.second.request.point.TransportPoint;
import lombok.Data;

import java.util.List;

@Data
public class TransportHistoryDto {
    private Integer treeId;
    private List<TransportPoint> points;
}
