package com.canzhang.sample.base

import android.app.Activity
import com.canzhang.sample.base.bean.ComponentItem

/**
 * Java代码转Kotlin代码
 * 打开要转的文件或者代码：
 * 工具栏 ：Code - Convert Java File To Kotlin File

 * Kotlin代码 转 Java代码
 * 工具栏：Tools > Kotlin > Show Kotlin Bytecode
 * 最后点击 Decompile
 */
interface IManager {
    //？ 表示该参数可以为空
    fun getSampleItem(activity: Activity?): List<ComponentItem?>?
    fun getPriority(): Int
}


//public interface IManager {
//    List<ComponentItem> getSampleItem(Activity activity);
//    int getPriority();
//}
