package com.canzhang.sample.manager.zhujie;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试注解相关内容
 */
@MarkManager(value = "注解测试")
public class ZhuJieManager extends BaseManager {

    public static final int CHECK_METHOD_COMPLETE = 1;//校验方法完整性
    public static final int TEST_METHOD = 2;//调试模式（测试方法调用）
    public static final int RELEASE = 0;//正式环境使用
    //调试模式
    public static @ModeType
    int mModeType = RELEASE;
    private Activity mActivity;

    /**
     * 调试模式 0、正式使用（默认模式） 1、校验方法是否完整实现 2、调试方法
     */
    @IntDef({CHECK_METHOD_COMPLETE, TEST_METHOD, RELEASE})
    @Retention(RetentionPolicy.SOURCE)//定义仅在源码中保留，编译后自动移除
    public @interface ModeType {
    }

    public static void setModeType(@ModeType int type) {
        mModeType = type;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(testGet());
        list.add(testBind());

        return list;
    }


    private ComponentItem test() {
        return new ComponentItem("注解优雅用法", "注解的简单用法：如何利用注解优雅的传递固定的几个数值示例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setModeType(1);//报错,此报错不影响实际应用，仅做提示
                setModeType(ZhuJieManager.CHECK_METHOD_COMPLETE);//正常
            }
        });
    }

    @Documented //文档
    @Retention(RetentionPolicy.RUNTIME)//运行时注解
    @Target(ElementType.METHOD)//修饰方法
    @Inherited//自动继承
    public @interface MethodInfo {
        String author() default "Peabody";

        String date();

        int version() default 1;

        String[] content() default {"content_test","hahah"};

    }

    @MethodInfo(//注解也是可以被混淆的，要特别留意这个事项。
            author = "zhangcan",
            date = "2020年9月20日01:16:00",
            content = "66666")//数组可以直接写成字符串，也可以直接写成{"a","b"}
    public String getDescription() {
        return "no description";
    }

    private ComponentItem testGet() {
        return new ComponentItem("运行时注解内容获取", "通过反射获取注解内容\n需要特别注意注解也是可以被混淆的，如果不想被混淆注意keep", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Method[] methods = ZhuJieManager.class.getMethods();
//                try {
//                    methods =  Class.forName("com.canzhang.sample.manager.zhujie.ZhuJieManager").getMethods();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
                for (Method method : methods) {
                    log("运行时注解内容获取 当前方法名：" + method.getName());
                    MethodInfo annotation = method.getAnnotation(MethodInfo.class);
                    if (annotation != null) {
                        log("author:" + annotation.author());
                        log("date:" + annotation.date());
                        log("version:" + annotation.version());
                        log("content:" + annotation.content());//获取的是一个对象String[]
                        log("content:" + annotation.content()[0]);//第一个
                    }

                }

            }
        });
    }

    private ComponentItem testBind() {
        return new ComponentItem("CLASS注解 编译生成类（butterknife原理）", "class注解：注解会被编译器记录在 class 文件中，但不需要被 VM 保留到运行时", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //测试通过注解获取的view设置文字是否成功
                mActivity.startActivity(new Intent(mActivity, BindTestActivity.class));
            }
        });
    }


    public static Map<String, Object> getAllManager(){
        try {
            Class<?> aClass = Class.forName("com.canzhang.zhujie.test.Manger_Map_Auto_Generate");
            Method getAllManager = aClass.getMethod("getAllManager");
            Map<String,Object> map = (Map<String, Object>) getAllManager.invoke(aClass);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getAllManager",Log.getStackTraceString(e));
        }
        return null;
    }

}
