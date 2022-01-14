package com.bmts.heating.stream.flink.handle;

import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.google.gson.Gson;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: TestFlatMap
 * @Description: 测试 flink
 * @Author: pxf
 * @Date: 2021/7/21 16:59
 * @Version: 1.0
 */

public class SecondRichMap extends RichMapFunction<Message_Gather, Message_Gather> {

    private static Logger logger = LoggerFactory.getLogger(SecondRichMap.class);

    private static final long serialVersionUID = 1L;

    private static Gson gson = new Gson();

    // 可以进行初始化spring 容器
    @Override
    public void open(Configuration parameters) throws Exception {
//        super.open(parameters);
    }

    // 处理业务
    @Override
    public Message_Gather map(Message_Gather messageGather) throws Exception {
        logger.info("------Second-------------------- 接收对象数据----- {}", messageGather.getRelevanceId());
        return messageGather;
    }

    // 在关闭之前执行的一些操作
    @Override
    public void close() throws Exception {
//        super.close();
    }


}
