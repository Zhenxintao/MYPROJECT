package com.bmts.heating.commons.basement.model.db.entity;

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
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Expressions implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 表达式key
     */
    private String expressionkey;

    /**
     * 表达式内容
     */
    private String content;

    /**
     * 1.单表达式 2.多表达式
     */
    private Integer type;

    private String name;

    /**
     * 表达式描述
     */
    private String description;


}
