package com.bmts.heating.monitor.dirver.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonitorProtery {

    private MonitorType.Pattern pattern;
    private List<MonitorMuster.Plugin> pluginList= new ArrayList<MonitorMuster.Plugin>();
}
