package com.example.simple_test_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)//存在字节码中，但是不会保留到运行时
@Target(ElementType.TYPE)//(类、接口(包括注解类型)、枚举的声明)
public @interface MarkManager {
    String value() default "未命名，请在注解上命名";
}