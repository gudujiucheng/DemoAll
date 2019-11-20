#include <stdio.h>
#include <jni.h>


#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_canzhang_sample_manager_jni_JNICmake_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
