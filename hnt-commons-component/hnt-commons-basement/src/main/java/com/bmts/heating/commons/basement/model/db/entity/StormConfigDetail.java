package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author naming
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stormConfigDetail")
public class StormConfigDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 节点名称
     */
    @TableField("nodeName")
    private String nodeName;

    /**
     * 1.本地随机 2.随机
     */
    @TableField("nodeStrategy")
    private Integer nodeStrategy;

    /**
     * 节点标识
     */
    @TableField("nodeFlag")
    private String nodeFlag;

    /**
     * 中文名称
     */
    @TableField("nodeCnName")
    private String nodeCnName;

    /**
     * 1.本地jar包 2.http 3.grpc
     */
    private Integer type;

    /**
     * jar包形式读取bean名称
     */
    @TableField("beanName")
    private String beanName;

    /**
     * 暂时定义为接口地址
     */
    @TableField("httpConfig")
    private String httpConfig;

    /**
     * 预留
     */
    @TableField("grpcConfig")
    private String grpcConfig;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 分组名称
     */
    @TableField("groupName")
    private String groupName;

    @TableField("stormConfigId")
    private Integer stormConfigId;
}
