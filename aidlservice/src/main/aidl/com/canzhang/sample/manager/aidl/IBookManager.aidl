// IBookManager.aidl   设置让客户端允许调用的接口
package com.canzhang.sample.manager.aidl;

// 系统会自动生成对应的java文件：com.canzhang.sample.manager.aidl.IBookManager.java

//导入所需要使用的非默认支持数据类型的包,就是在同一个包内，也是需要导包的
import com.canzhang.sample.manager.aidl.Book;

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);

}
