package com.company.project.inter.common;

import com.company.project.common.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/2/23.
 * 使用自定义注解用于标记是哪种路线观测类型
 */

@Target(ElementType.TYPE)   //表示只能给类添加该注解
@Retention(RetentionPolicy.RUNTIME)  //这个必须要将注解保留在运行时
public @interface ObserverRegion {

    //String ORFINAL(),string为ORFINAL的类型，必须要有()
    String ORFINAL() default Constants.TYPE1;
}
