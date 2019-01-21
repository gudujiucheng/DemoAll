#include<jni.h>
#include<stdio.h>
//导入我们创建的头文件,注意包名要正确
#include "com_canzhang_sample_manager_jni_JNITest.h"

//注意包名不要弄错
JNIEXPORT jstring Java_com_canzhang_sample_manager_jni_JNITest_get
  (JNIEnv *env, jclass jclass){

      //返回一个字符串
      return (*env)->NewStringUTF(env,"This is my first NDK Application");
  }

