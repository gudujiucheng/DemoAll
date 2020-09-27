package com.example.simplebutterknife_compiler;

import com.example.simplebutterknife_annotations.BindView;
import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
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
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * 注解处理器
 * <p>
 * 利用了 Google 的 AutoService 为注解处理器自动生成 metadata 文件并将注解处理器jar文件加入构建路径，
 * 这样也就不需要再手动创建并更新 META-INF/services/javax.annotation.processing.Processor 文件了
 *参考 java sip：https://www.jianshu.com/p/deeb39ccdc53
 *
 */
@AutoService(Processor.class)
public class SimpleButterKnifeProcessor extends AbstractProcessor {

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
        types.add(BindView.class.getCanonicalName());
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
         * 需要为每个包含注解的 Activity 都生成一个对应的 _ViewBinding 文件，
         * 所以使用 Map 来存储。BindingSet 存储 Activity 信息和它的 View 绑定信息，
         * View 绑定信息（ViewBinding）包括绑定 View 的类型、View 的 ID 以及 View 的变量名。
         */
        Map<TypeElement, BindingSet> bindingMap = new LinkedHashMap<>();
        /**
         * 查找所有被 @BindView 注解的程序元素(Element)，为了简化，
         * 这里只认为被注解的元素是 View 字段且它的外层元素(EnclosingElement)为 Activity 类:
         */
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            // 注解元素的外侧元素，即 View 的所在 Activity 类
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            // 注解的 value 值，即 View 的 id
            int id = element.getAnnotation(BindView.class).value();
            // 注解元素的名字，即 View 变量名
            Name simpleName = element.getSimpleName();
            String name = simpleName.toString();
            // 注解元素的类型，即 View 的类型
            TypeMirror elementType = element.asType();
            TypeName type = TypeName.get(elementType);

            //然后把这些信息存到 Activity 对应的 View 绑定中:
            BindingSet bindingSet = bindingMap.get(enclosingElement);
            if (bindingSet == null) {
                bindingSet = new BindingSet();
                TypeMirror typeMirror = enclosingElement.asType();
                TypeName targetType = TypeName.get(typeMirror);
                String packageName = MoreElements.getPackage(enclosingElement).getQualifiedName().toString();
                String className = enclosingElement.getQualifiedName().toString().substring(
                        packageName.length() + 1).replace('.', '$');
                ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");
                bindingSet.targetTypeName = targetType;
                bindingSet.bindingClassName = bindingClassName;
                bindingMap.put(enclosingElement, bindingSet);
            }
            if (bindingSet.viewBindings == null) {
                bindingSet.viewBindings = new ArrayList<>();
            }
            ViewBinding viewBinding = new ViewBinding();
            viewBinding.type = type;
            viewBinding.id = id;
            viewBinding.name = name;
            bindingSet.viewBindings.add(viewBinding);
            /**
             * 确定完 Activity 信息和它对应的 View 绑定信息后，为每个 Activity 生成对应的 XXX_ViewBinding.java 文件，
             * 文件内容就是前面所说类绑定类
             *
             * 虽然通过字符串拼接可以拼出这样的文件内容，但我们还得考虑 import，还得考虑大括号和换行，
             * 甚至还得考虑注释和代码美观，所以利用 JavaPoet 来生成 .java 文件是个不错的选择:
             */

            for (Map.Entry<TypeElement, BindingSet> entry : bindingMap.entrySet()) {
                TypeElement typeElement = entry.getKey();
                BindingSet binding = entry.getValue();

                TypeName targetTypeName = binding.targetTypeName;
                ClassName bindingClassName = binding.bindingClassName;
                List<ViewBinding> viewBindings = binding.viewBindings;
                /**
                 * 生成binding类 public class MainActivity_ViewBinding {}
                 */
                TypeSpec.Builder viewBindingBuilder = TypeSpec.classBuilder(bindingClassName.simpleName())
                        .addModifiers(Modifier.PUBLIC);
                /**
                 * 生成binding类中的成员变量 public MainActivity target;
                 */
                // public的target字段用来保存 Activity 引用
                viewBindingBuilder.addField(targetTypeName, "target", Modifier.PUBLIC);
                /**
                 * 生成构造函数
                 *
                 *   public MainActivity_ViewBinding(MainActivity target) {
                 *     this(target, target.getWindow().getDecorView());
                 *   }
                 */
                MethodSpec.Builder activityViewBuilder = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(targetTypeName, "target");
                activityViewBuilder.addStatement("this(target, target.getWindow().getDecorView())");
                viewBindingBuilder.addMethod(activityViewBuilder.build());

                /**
                 * 生成构造函数2
                 *
                 * public MainActivity_ViewBinding(MainActivity target, View source) {
                 *     this.target = target;
                 *
                 *     target.mTitleTextView = (TextView) source.findViewById(2131165300);
                 *   }
                 */
                MethodSpec.Builder viewBuilder = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(targetTypeName, "target")
                        .addParameter(ClassName.get("android.view", "View"), "source");
                viewBuilder.addStatement("this.target = target");
                viewBuilder.addCode("\n");
                for (ViewBinding temp : viewBindings) {//这里就是遍历所有要绑定的元素，进行findviewbyId
                    CodeBlock.Builder builder = CodeBlock.builder()
                            .add("target.$L = ", temp.name);
                    builder.add("($T) ", temp.type);
                    builder.add("source.findViewById($L)", CodeBlock.of("$L", temp.id));
                    viewBuilder.addStatement("$L", builder.build());
                }
                viewBindingBuilder.addMethod(viewBuilder.build());
                // 输出 Java 文件
                JavaFile javaFile = JavaFile.builder(bindingClassName.packageName(), viewBindingBuilder.build())
                        .build();
                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }

        }

        return false;
    }

    class BindingSet {
        TypeName targetTypeName;
        ClassName bindingClassName;
        List<ViewBinding> viewBindings;
    }

    class ViewBinding {
        TypeName type;
        int id;
        String name;
    }
}