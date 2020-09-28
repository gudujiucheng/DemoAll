package com.example.simple_test_compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * 测试java poet的使用
 * <p>
 * JavaPoet生成的是源代码而不是字节码，所以可以通过阅读源码确保正确。
 */
public class JavaPoetTest {

    public static void main(String[] args) throws Exception {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        MethodSpec test = MethodSpec.methodBuilder("test")
                .addCode(""
                        + "int total = 0;\n"
                        + "for (int i = 0; i < 10; i++) {\n"
                        + "  total += i;\n"
                        + "}\n")
                .build();

        MethodSpec test2 = MethodSpec.methodBuilder("test2")
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total += i")
                .endControlFlow()
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addMethod(test)
                .addMethod(test2)
                .build();

        JavaFile javaFile = JavaFile.builder("com.canzhang.javapoet.test", helloWorld)
                .build();

        try {
//            javaFile.writeTo(System.out);//输出到控制台
            javaFile.writeTo(new File("JavaPoetTest_Generate"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}