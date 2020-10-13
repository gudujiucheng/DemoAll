package com.canzhang.sample.manager.kotlin

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.canzhang.sample.base.BaseManager
import com.canzhang.sample.base.bean.ComponentItem
import com.example.simple_test_annotations.MarkManager

@MarkManager(value = "kotlin 测试")//另外如果注解要支持到kotlin代码，需要使用 kapt代替annotationProcessor 引入注解处理器
class KotlinKTestManager : BaseManager() {

    //kotlin的默认修饰符是 public 所以如果是public 则不用修饰了
    public var v = "任何地方都可以见"
    private var v2 = "只有在本源文件中可见"

    /**
     * 一个ide创建的 Module
     * 一个Maven或者Gradle项目
     * 通过一次调用Ant任务编译的一组文件
     */
    internal val v3 = "同一模块下可见"//TODO 这个释义 需要理解下

    override fun getSampleItem(activity: Activity?): MutableList<ComponentItem> {
//        super.getSampleItem(activity)
        //集合定义方法
        val itemList = mutableListOf(testAddItem(), testAddItem02(), testAddItem03(), testAddItem04(), testAddItem05());
        return itemList;
    }

    override fun getPriority(): Int {
        return 6
    }

    private fun testAddItem05(): ComponentItem {//返回值的写法 也是一个冒号 后面加上返回值类型
        return ComponentItem("静态修饰符", View.OnClickListener {
            showToast(StaticTest.nameStatic)
            StaticTest.speakStatic("调用静态方法");


            //java 调用则必须要通过 instance 参见 JavaKotlinTest
//            KotlinKTestManager.StaticTest.instance.getNameStatic();
//            KotlinKTestManager.StaticTest.instance.speakStatic("哈哈哈哈");
        })
    }

    class StaticTest {
        var name: String = "AA"

        fun speak() {}

        //静态
        companion object instance{//参考：https://www.jianshu.com/p/a5b0da8bbba3?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation

            var nameStatic: String = "BB"

            fun speakStatic(tips: String) {
                print(tips)
            }

        }
    }

    private fun testAddItem04(): ComponentItem {//返回值的写法 也是一个冒号 后面加上返回值类型
        return ComponentItem("空指针", View.OnClickListener {
            //一样会崩溃，如果上面activity 不赋值的话
//           val applicationContext = mActivity.applicationContext//这里有个坑，如果BaseManager 是java 则这一行不会报错，只有是kotlin的时候才会报错

            var name: String? = "哈哈哈"//这种带有？号的标示可以为空
            name = null;
            val len = name?.length//只有当name不为null的时候才调用   不加上？号就会报错
            log("哈哈哈哈-->>name：$len")//这里打印的len 是  null


            //Elvis 操作符 ?:
            //在你的表达式为null的时候执行 ?:后面的操作
            val len2 = name?.length ?: 666
            log("哈哈哈哈-->>name：$len2")//这样打印的是666  有点类似于三元表达式


            var str: String//不带？号的话，赋值null或者可为空的变量 编译器都会报错
            str = "666"
//            str = name;//报错
//            str = null;//报错
            str = name!!//不报错  当你操作的对象为空的时候，抛出空指针异常！ KotlinNullPointerException
            val length = str.length;
            log("哈哈哈哈-->>str：$length")


        })

    }

    private fun testAddItem(): ComponentItem {//返回值的写法 也是一个冒号 后面加上返回值类型
        return ComponentItem("创建一个对象，并调用方法", View.OnClickListener {
            TestClass().log()
            TestClassWithArgs("World!  from kotlin").log()
        })

    }

    private fun testAddItem02(): ComponentItem {
        return ComponentItem("测试接口类", View.OnClickListener {
            log("name:" + TestInterface().name)
            TestInterface().bar()
            TestInterface().foo()
            TestInterface().test()
        })
    }

    private fun testAddItem03(): ComponentItem {
        return ComponentItem("继承测试", View.OnClickListener {
            val s = Student("Runoob", 18, "S12346", 89)
            println("学生名： ${s.name}")
            println("年龄： ${s.age}")
            println("学生号： ${s.no}")
            println("成绩： ${s.score}")
        })
    }


    //open表示可继承（不添加open则默认是final）,extends也被改为:（和实现接口一样,可以继续加接口 顺序无要求）
    open class Person(var name: String, var age: Int) {// 基类

    }

    //如果子类有主构造函数， 则基类必须在主构造函数中立即初始化。
    class Student(name: String, age: Int, var no: String, var score: Int) : Person(name, age) {//子类

    }


    class MyView : View {
        //如果没有主构造函数，则需要在子构造函数调用基类构造函数
        constructor(ctx: Context) : super(ctx)
        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    }


    interface MyInterface {
        fun bar()   // 未实现
        fun foo() {  //已实现
            // 可选的方法体
            println("foo 方法被调用")
        }
    }

    interface MyInterface02 {
        //接口中的属性只能是抽象的，不允许初始化值，接口不会保存属性值，实现接口时，必须重写属性：
        var name: String
        fun test();
    }

    class TestInterface : MyInterface, MyInterface02 {
        //一个类或者对象可以实现一个或多个接口
        override fun bar() {
            println("var 方法:$name")
        }

        override var name: String
            //强制重写
            get() = "哈哈哈哈哈哈"
            set(value) {}

        override fun test() {
            println("test 方法:$name")
        }

    }


    //无参数的
    class TestClass() {
        //似乎直接就变成员变量了，不用额外声明了。
        fun log() {
            println("Hello 无参数")
        }
    }

    //有参数的
    class TestClassWithArgs(private val name: String) {
        //似乎直接就变成员变量了，不用额外声明了。
        fun log() {
            println("Hello, $name")
        }
    }


}

//而在kotlin 中 main()要拿到 class  xx{  } 之外，这样才可以鼠标右键触发调用，这一点和java 不同
fun main(args: Array<String>) {
    // 创建一个对象不用 new 关键字
    KotlinKTestManager.TestClassWithArgs("World!  from kotlin").log()
}