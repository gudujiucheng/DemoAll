package com.example.simple_test_compiler;

import com.example.simple_test_annotations.MarkManager;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

/**
 * 注解处理器
 * <p>
 * 利用了 Google 的 AutoService 为注解处理器自动生成 metadata 文件并将注解处理器jar文件加入构建路径，
 * 这样也就不需要再手动创建并更新 META-INF/services/javax.annotation.processing.Processor 文件了
 * 参考 java sip：https://www.jianshu.com/p/deeb39ccdc53
 * <p>
 * java poet的使用 参考 https://blog.csdn.net/l540675759/article/details/82931785
 */
@AutoService(Processor.class)
public class MarkManagerProcessor extends AbstractProcessor {//FIXME 暂未实现

    /**
     * 方法指定可以支持最新的 Java 版本（直接指定最新的就可以了）
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 方法指定该注解处理器用于处理哪些注解（我们这里只处理 @BindView 注解）
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        //本例子 就是 处理MarkManager
        types.add(MarkManager.class.getCanonicalName());
        return types;
    }

    /**
     * 检索注解元素并生成代码的是 process 方法的实现:
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        /**
         * TypeSpec————用于生成类、接口、枚举对象的类
         */
        TypeSpec.Builder managerMapClassBuilder = TypeSpec.classBuilder("Manger_Map_Auto_Generate")
                .addModifiers(Modifier.PUBLIC);

        managerMapClassBuilder.addField(ParameterizedTypeName.get(Map.class, String.class,Object.class), "sManagerMap", Modifier.PUBLIC, Modifier.STATIC);

        MethodSpec.Builder initManagerMethodBuilder = MethodSpec.methodBuilder("initManager")
                .addModifiers(Modifier.PRIVATE)
                .addModifiers(Modifier.STATIC);


        MethodSpec.Builder addManagerMethodBuilder = MethodSpec.methodBuilder("addManager")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassName.get("java.lang", "String"), "desc")
                .addParameter(TypeName.OBJECT, "target")
                .addStatement("sManagerMap.put(desc,target)");


        for (Element element : roundEnvironment.getElementsAnnotatedWith(MarkManager.class)) {
            //manager 类的描述
            String managerDesc = element.getAnnotation(MarkManager.class).value();
            System.out.println("--------------->>>>>管理类描述：" + managerDesc);
            // 注解元素的名字，即 Manager类名
            Name simpleName = element.getSimpleName();
            String name = simpleName.toString();
            System.out.println("--------------->>>>>注解类的类名：" + name);//ZhuJieManager

            initManagerMethodBuilder.addStatement("addManager($S,$S)",managerDesc,simpleName);// addManager("注解相关测试例子");

        }


        TypeSpec managerMapClass = managerMapClassBuilder
                .addMethod(addManagerMethodBuilder.build())
                .addMethod(initManagerMethodBuilder.build())
                .build();


        JavaFile file = JavaFile.builder("com.test", managerMapClass).build();

        try {
//                file.writeTo(System.out);
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            System.out.println("--------------->>>>>生成类异常：" + e.getMessage());
            e.printStackTrace();
        }


        return false;
    }


}