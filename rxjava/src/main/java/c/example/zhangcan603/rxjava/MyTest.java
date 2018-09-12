package c.example.zhangcan603.rxjava;

import android.annotation.SuppressLint;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyTest {
    public static void main(String[] args){
        test();

    }



    @SuppressLint("CheckResult")
    private static void test(){
        System.out.print("开始————————》》》》");
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) {
                emitter.onNext("开始发送事件1");
                emitter.onNext("开始发送事件2");
                emitter.onNext("开始发送事件3");
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.print("接收到事件："+s+"\n");
            }
        });

    }

}
