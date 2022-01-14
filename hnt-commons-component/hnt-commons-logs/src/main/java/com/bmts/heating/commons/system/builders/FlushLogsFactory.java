package com.bmts.heating.commons.system.builders;

import com.bmts.heating.commons.system.exception.TsccLogException;
import com.bmts.heating.commons.system.pojo.LogDispose;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FlushLogsFactory {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LogEvent builder(){
        return new LogEvent();
    }

    @Data
    public class LogEvent {
        private LogDispose.LogLevel logLevel;   //日志等级
        private LogDispose.LogType  logType;    //日志分类

        private LogDispose.LogMotion    logMotion;  //日志动作
        private LogDispose.LogDestination   logDestination;  //日志存储
        private String describe;    //描述
        private Exception   exception;  //异常信息
        private String operator;    //操作人
        private String appearTime;  //产生时间

        public void pushLogs(){
            System.out.println(this.describe);
        }

        /**
         * 产生日志事件
         * @param operator
         * @param logType
         * @param logMotion
         * @param obj
         * @return
         */
        public LogEvent createLogs(String operator, LogDispose.LogType logType, LogDispose.LogMotion logMotion, Object... obj) throws TsccLogException {
            if(obj.length<=3){
                this.setOperator(operator);
                this.setLogType(logType);
                this.setLogMotion(logMotion);
                for(Object object :obj){
                    if (object instanceof LogDispose.LogLevel) {
                        //日志等级
                        this.setLogLevel((LogDispose.LogLevel)object);
                    }else if(object instanceof LogDispose.LogDestination){
                        //日志去向
                        this.setLogDestination((LogDispose.LogDestination)object);
                    }else if(object instanceof String){
                        //描述
                        this.setDescribe(object.toString());
                    }else{
                        throw  new TsccLogException("自定义日志参数类型错误..............");
                    }
                }
                if(this.getLogLevel()==null)
                    this.setLogLevel(LogDispose.LogLevel.Primary);
                if(this.getLogDestination()==null)
                    this.setLogDestination(LogDispose.LogDestination.GiveUp);
                if(this.getDescribe()==null||"".equals(this.getDescribe()))
                    this.setDescribe("");
                this.setAppearTime(sdf.format(new Date()));
                return this;
            }else{
                throw new TsccLogException("自定义日志参数个数错误..............");
            }
        }
    }
}
