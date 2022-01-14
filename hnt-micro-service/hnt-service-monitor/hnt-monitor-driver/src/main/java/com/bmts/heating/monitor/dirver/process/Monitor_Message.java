package com.bmts.heating.monitor.dirver.process;

import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component("Monitor_Message")
@Scope("prototype")
@Description("失效数据消息")
public class Monitor_Message implements Serializable {

    private String identity;    //当前节点标识
    private MonitorType.Pattern pattern;
    private MonitorMuster.Plugin plugin;
    private String handler; //处理类
}
