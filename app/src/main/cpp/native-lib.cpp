#include <jni.h>
#include <string>
#include <iostream>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_rkrit_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
