package com.example.administrator.demoall.bean.xuliehua;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * android 序列化方式  使用简单
 */
public class ParcelableBean implements Parcelable{
    public String name;
    public  int age;


    //内容描述接口，基本不用管，返回0就行了
    @Override
    public int describeContents() {
        return 0;
    }

    //该方法将类的数据写入外部提供的Parcel中.即打包需要传递的数据到Parcel容器保存，以便从parcel容器获取数据
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        dest.writeString(name);
        dest.writeInt(age);

    }



    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Creator<ParcelableBean> CREATOR = new Creator<ParcelableBean>(){

//       从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层
        @Override
        public ParcelableBean createFromParcel(Parcel source) {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ParcelableBean p = new ParcelableBean();
            p.name = source.readString();
            p.age = source.readInt();
            return p;
        }

//       方法是供外部类反序列化本类数组使用
        @Override
        public ParcelableBean[] newArray(int size) {
            return new ParcelableBean[size];
        }
    };

    @Override
    public String toString() {
        return "ParcelableBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
