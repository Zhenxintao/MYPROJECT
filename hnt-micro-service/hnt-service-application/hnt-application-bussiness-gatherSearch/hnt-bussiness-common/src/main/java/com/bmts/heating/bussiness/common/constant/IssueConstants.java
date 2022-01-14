package com.bmts.heating.bussiness.common.constant;

/**
 * 控制 常量类
 */
public class IssueConstants {

    // 二次采集数据
    public class Snd {

        //二次回压  二次补水变频泵、  泄压的回水压力
        public static final String SND_P2H = "P2h";

        //二次供温  一次调节阀实时供温
        public static final String SND_T2G = "T2g";

        //二次供回压差  二次循环泵
        public static final String SND_P2D = "P2D";

        //二次供压   二次循环泵
        public static final String SND_P2G = "P2g";


    }

    // 二次补水变频泵
    public class BPMode {
        //二次补水本地远程状态
        public static final String BP_LONG_STATUS = "BP101";
        //二次补水变频运行状态
        public static final String BP_RUN_STATUS = "BP102";
        //二次补水变频故障状态
        public static final String BP_FAULT_STATUS = "BP103";
        //补水泵频率给定
        public static final String BP_SET_HZ = "BP_SV";
        //补水泵频率反馈
        public static final String BP_REAL_HZ = "BP1_Hz";
        //补水泵电流反馈
        public static final String BP_REAL_ELECTRIC = "BP1_A";


        //二次补水变频工作方式
        public static final String WORK_BP_Mode = "BP_Mode";
        //二次补水变频  定值频率设定值
        public static final String WORK_BP_SET_HZ = "BP_MSP";
        //二次补水变频  二次回压设定值
        public static final String WORK_BP_SET_P2H = "BP_P2hSP";
        //二次补水变频  自动高限设定值
        public static final String WORK_BP_SET_P2H_H = "BP_P2h_H";
        //二次补水变频  自动低限设定值
        public static final String WORK_BP_SET_P2H_L = "BP_P2h_L";

        //二次补水变频  1机组1#补水启动令
        public static final String WORK_BP1_START = "BP1_Start";
        //二次补水变频  1机组1#补水停止令
        public static final String WORK_BP1_STOP = "BP1_Stop";
        //二次补水变频  1机组2#补水启动令
        public static final String WORK_BP2_START = "BP2_Start";
        //二次补水变频  1机组2#补水停止令
        public static final String WORK_BP2_STOP = "BP2_Stop";
    }

    // 一次调节阀
    public class CVMode {
        //一次阀门给定开度
        public static final String CV_SET_OPEN = "CV1_SV";
        //一次阀门实际开度
        public static final String CV_REAL_OPEN = "CV1_U";

        //一次调节阀工作方式
        public static final String WORK_CV_Mode = "CV1_Mode";

        //一次阀门  定值开度设定值
        public static final String WORK_CV_SET_OPEN = "CV1_MSP";


        //一次阀门  恒定二次供温设定值
        public static final String WORK_CV_SET_T2GSP = "CV1_T2gSP";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_0 = "T2g_SP_Time_0";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_1 = "T2g_SP_Time_1";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_2 = "T2g_SP_Time_2";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_3 = "T2g_SP_Time_3";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_4 = "T2g_SP_Time_4";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_5 = "T2g_SP_Time_5";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_6 = "T2g_SP_Time_6";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_7 = "T2g_SP_Time_7";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_8 = "T2g_SP_Time_8";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_9 = "T2g_SP_Time_9";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_10 = "T2g_SP_Time_10";
        //一次阀门  二次供温12时段偏差设定值
        public static final String WORK_CV_SET_T2GSP_TIME_11 = "T2g_SP_Time_11";


        //一次阀门  恒定流量设定值
        public static final String WORK_CV_SET_FLOW = "CV1_F1SP";
        //一次阀门  一次调节阀设置步进
        public static final String WORK_CV_SET_STEP = "CV1_Step";
        //一次阀门  一次调节阀最小开度
        public static final String WORK_CV_SET_MIN = "CV1_Min";
        //一次阀门  一次调节阀最大开度
        public static final String WORK_CV_SET_MAX = "CV1_Max";

    }

    // 二次循环泵
    public class XPMode {
        //二次循环泵本地远程状态
        public static final String XP_LONG_STATUS = "XP101";
        //二次循环泵变频运行状态
        public static final String XP_RUN_STATUS = "XP102";
        //二次循环泵变频故障状态
        public static final String XP_FAULT_STATUS = "XP103";
        //循环泵频率给定
        public static final String XP_SET_HZ = "XP_SV";
        //循环泵频率反馈
        public static final String XP_REAL_HZ = "XP1_Hz";
        //循环泵电流反馈
        public static final String XP_REAL_ELECTRIC = "XP1_A";


        //二次循环泵变频工作方式
        public static final String WORK_XP_Mode = "XP_Mode";
        //二次循环变频  定值频率设定值
        public static final String WORK_XP_SET_HZ = "XP_MSP";
        //二次循环变频  二次供回压差设定值
        public static final String WORK_XP_SET_P2D = "XP_P2DSP";
        //二次循环变频  二次供压设定值
        public static final String WORK_XP_SET_P2G = "XP_P2gSP";


        //二次循环变频  1机组1#循环启动令
        public static final String WORK_XP1_START = "XP1_Start";
        //二次循环变频  1机组1#循环停止令
        public static final String WORK_XP1_STOP = "XP1_Stop";

        //二次循环变频  1机组2#循环启动令
        public static final String WORK_XP2_START = "XP2_Start";
        //二次循环变频  1机组2#循环停止令
        public static final String WORK_XP2_STOP = "XP2_Stop";
    }

    // 泄压阀
    public class XSVMode {

        //泄压阀状态
        public static final String XSV_RUN_STATUS = "XSV_ON";


        //泄压电磁阀工作方式
        public static final String WORK_XSV_Mode = "XSV_Mode";
        //泄压电磁阀工作方式0手动启停控制(0:关闭 1开启)
        public static final String WORK_XSV_SET_HAND = "XSV_SS";
        //泄压电磁阀  自动高限设定值
        public static final String WORK_XSV_SET_P2H_H = "XSV_P2h_H";
        //泄压电磁阀  自动低限设定值
        public static final String WORK_XSV_SET_P2H_L = "XSV_P2h_L";


    }

}
