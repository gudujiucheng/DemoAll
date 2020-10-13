package com.canzhang.sample.manager.kotlin

import android.app.Activity
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
        super.getSampleItem(activity)
        //集合定义方法
        val itemList = mutableListOf(testAddItem(), testAddItem02());
        return itemList;
    }

    override fun getPriority(): Int {
        return 6
    }

    private fun testAddItem02(): ComponentItem {
        return ComponentItem("测试接口类", View.OnClickListener {
            log("name:" + TestInterface().name)
            TestInterface().bar()
            TestInterface().foo()
            TestInterface().test()
        })
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

        override var name: String//强制重写
            get() = "哈哈哈哈哈哈"
            set(value) {}

        override fun test() {
            println("test 方法:$name")
        }

    }

    private fun testAddItem(): ComponentItem {//返回值的写法 也是一个冒号 后面加上返回值类型
        return ComponentItem("创建一个对象，并调用方法", View.OnClickListener {
            TestClass().log()
            TestClassWithArgs("World!  from kotlin").log()
        })

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