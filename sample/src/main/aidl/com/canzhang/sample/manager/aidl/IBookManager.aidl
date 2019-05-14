// IBookManager.aidl   设置让客户端允许调用的接口
package com.canzhang.sample.manager.aidl;

// 系统会自动生成对应的java文件：com.canzhang.sample.manager.aidl.IBookManager.java

//导入所需要使用的非默认支持数据类型的包,就是在同一个包内，也是需要导包的
import com.canzhang.sample.manager.aidl.Book;

interface IBookManager {

    List<Book> getBookList();


    //除了基本类型数据，其它类型的参数必须标上方向：in、out、inout。in 表示输入；out 表示输出；inout 表示输入输出型的参数，注意按需使用，因为 out 以及 inout 在底层实现是需要一定开销的。
    void addBook(in Book book);

}
