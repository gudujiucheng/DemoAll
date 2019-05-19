package com.canzhang.aidlservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.canzhang.sample.manager.aidl.Book;
import com.canzhang.sample.manager.aidl.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * aidl 服务端
 * 原文：https://blog.csdn.net/luoyanglizi/article/details/51980630
 */
public class AIDLService extends Service {
    public final String TAG = "AidlClientFragment";

    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL文件生成的BookManager
    private final IBookManager.Stub mBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "log from service :invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }


        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "log from service :Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "log from service :invoking addBooks() method , now the list is : " + mBooks.toString());
            }
        }
    };

    private static int position;

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("服务端的书 "+position);
        book.setPrice(28);
        position++;
        mBooks.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, String.format("log from service : on bind,intent = %s", intent.toString()));
        return mBookManager;
    }

}