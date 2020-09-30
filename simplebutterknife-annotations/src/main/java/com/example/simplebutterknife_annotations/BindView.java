package com.example.simplebutterknife_annotations;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明注解
 */
@Retention(RetentionPolicy.CLASS)//存在字节码中，但是不会保留到运行时
@Target(ElementType.FIELD)//用于修饰字段
public @interface BindView {
    @IdRes int value();//入参是一个int值 IdRes表示资源类型的int值
}
