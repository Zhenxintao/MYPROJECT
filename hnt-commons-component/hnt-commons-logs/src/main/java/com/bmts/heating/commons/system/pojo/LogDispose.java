package com.bmts.heating.commons.system.pojo;

public class LogDispose {

    public enum LogMotion{
        Login,      //登录
        Quit,       //退出
        Add,        //增加
        Delete,     //删除
        Update,     //修改
        Select,    //查询
        Issue,  //下发
        Control,    //控制
        Execute,    //执行
        Gather,     //采集
        Divider,    //分配
        Clean   //清洗
    }
    public enum LogLevel{
        Primary,    //初级
        Intermediate,   //中级
        Senior,     //高级
        Superfine   //特级
    }

    public enum LogDestination{
        DataBase,   //关系数据库
        Hdfs,   //分布式文件系统
        Elasticsearch,  //非关系数据库
        File,   //文件
        GiveUp  //丢弃
    }

    public enum LogType{

        Manager,    //后台管理

        Actual,     //实时监控

        Alarms,     //报警

        Collect,       //采集

        Wash,      //清洗

        Balance    //全网平衡
    }
}
