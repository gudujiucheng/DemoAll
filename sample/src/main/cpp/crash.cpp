#include <stdio.h>
#include <jni.h>


/**
 * 引起 crash
 */
void fqlCrashTest() {
    volatile int *a = (int *) (NULL);
    *a = 1;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_fenqile_ui_myself_apptest_JNICrash_crash(JNIEnv *env, jobject obj) {
    fqlCrashTest();
}