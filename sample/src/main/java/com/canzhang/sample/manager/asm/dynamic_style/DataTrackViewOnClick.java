package com.canzhang.sample.manager.asm.dynamic_style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注意：此名称如果要改动，需要同步改动bury_point_sdk中对应的代码
 * 针对布局内设置android:onClick事件，常规方法是拿不到的
 * 需要在具体方法上面打上注解标记，然后visitAnnotation判断扫描到我们的注解，则植入代码
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTrackViewOnClick {
}
