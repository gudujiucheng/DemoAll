package com.example.simple_test_compiler;

import com.example.simple_test_annotations.CanTest;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;


@AutoService(Processor.class)
public class OtherTestProcessor extends AbstractProcessor {

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
        //本例子 就是 处理BINDView
        types.add(CanTest.class.getCanonicalName());
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
        TypeSpec.Builder managerMapClassBuilder = TypeSpec.classBuilder("Test_Generate")
                .addModifiers(Modifier.PUBLIC);
        ClassName StringType = ClassName.get("java.lang", "String");






        //返回使用给定注释类型注释的元素
        for (Element element : roundEnvironment.getElementsAnnotatedWith(CanTest.class)) {
            TypeMirror typeMirror = element.asType();//返回此元素定义的类型
            TypeName targetType = TypeName.get(typeMirror);
            //manager 类的描述
            String managerDesc = element.getAnnotation(CanTest.class).value();
            System.out.println("--------------->>>>>管理类描述：" + managerDesc);
            // 注解元素的名字，即 Manager类名
            Name simpleName = element.getSimpleName();
            String name = simpleName.toString();
            System.out.println("--------------->>>>>注解类的类名：" + name);//ZhuJieManager

            managerMapClassBuilder.addField(FieldSpec.builder(StringType, "sValue")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .initializer("$S", managerDesc)
                    .build());

        }



        TypeSpec managerMapClass = managerMapClassBuilder
                .build();

        JavaFile file = JavaFile.builder("com.canzhang.zhujie.test", managerMapClass).build();


        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            System.out.println("--------------->>>>>生成类异常：" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}