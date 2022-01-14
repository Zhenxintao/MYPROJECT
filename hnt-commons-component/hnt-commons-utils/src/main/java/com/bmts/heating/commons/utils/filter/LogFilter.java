package com.bmts.heating.commons.utils.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Arrays;

public class LogFilter extends Filter<ILoggingEvent> {
    private String flag;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {

        String[] split = flag.split(",");
        for (String s : split) {
            if (iLoggingEvent.getMessage().contains(s))
                return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;

    }
}
