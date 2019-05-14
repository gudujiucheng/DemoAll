
//参考文章：https://blog.csdn.net/luoyanglizi/article/details/51980630


// Book.aidl    为什么要有Book.aidl类，因为只有将类在aidl中声明时候，AIDL才能调用Book类(如果 AIDL 文件中使用了自定义的 parcelable 对象，那么必须新建一个和它同名的 AIDL 文件)
package com.canzhang.sample.manager.aidl;

parcelable Book;


