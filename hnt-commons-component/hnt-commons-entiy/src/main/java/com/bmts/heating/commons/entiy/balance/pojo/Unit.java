package com.bmts.heating.commons.entiy.balance.pojo;

import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 换热站机组信息
 * @description
 * @date 2021/1/29 12:20
 **/
@Data
@Description("全网平衡计算服务入参")
public class Unit {

    //换热站id
    public String  id ;

    //供水温度(℃)
    public float tg ;

    //回水温度(℃)
    public float th ;

    //补偿温度(℃)
    public float tc ;

    //供热面积(万㎡)
    public float area ;

    //阀门开度反馈(%)

    public float feedback;

    //时间戳
    public long timestamp ;

    //散热方式 1-挂暖 2-地暖
    public int radiatemethod ;

    //类型 1-阀门(% 间连) 2-变频器(Hz 分布式变频泵) 3-供温(℃ 混水)
    public int type ;

    //有效(参与全网平衡计算)
    public Boolean enable ;

    //true-工况校验正常 false-工况校验错误
    public Boolean isvalid ;

    //工况校验错误原因(仅在isvalid=false时有意义)
    public String  error ;

    //设定值
    public float setting ;

    //补偿后的阀门目标温度
    public float target ;

    //实际均温与补偿后目标值的差
    public float offset ;
}
