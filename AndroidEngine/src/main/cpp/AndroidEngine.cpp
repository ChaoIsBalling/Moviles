#include <jni.h>
#include <string>
#include "picosha2.h"
#include <iostream>
#include <vector>
// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("AndroidEngine");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("AndroidEngine")
//      }
//    }
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_androidengine_AndroidEngine_nativeHash(JNIEnv *env, jobject thiz, jstring s) {
    // TODO: implement nativeHash()
    const char *convertValue = env->GetStringUTFChars(s, nullptr);

    std::vector<unsigned char> hash(32);
    picosha2::hash256(convertValue, convertValue+ strlen(convertValue), hash.begin(), hash.end());

    std::string hex_str = picosha2::bytes_to_hex_string(hash.begin(), hash.end());

    return env->NewStringUTF(hex_str.c_str());
}