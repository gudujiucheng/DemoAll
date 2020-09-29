package com.example.simple_test_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅用于测试
 */
@Retention(RetentionPolicy.CLASS)//存在字节码中，但是不会保留到运行时
@Target(ElementType.TYPE)
public @interface CanTest {
    String value();
}
