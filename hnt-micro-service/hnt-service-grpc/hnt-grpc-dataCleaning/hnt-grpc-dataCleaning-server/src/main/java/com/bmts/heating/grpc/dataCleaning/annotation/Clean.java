package com.bmts.heating.grpc.dataCleaning.annotation;


import com.bmts.heating.grpc.dataCleaning.enums.DataCleanType;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Clean {

    // 清洗的操作类型
    DataCleanType cleanType();


}
