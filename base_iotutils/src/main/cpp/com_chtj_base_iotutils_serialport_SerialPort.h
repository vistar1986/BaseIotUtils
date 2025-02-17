/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_serialport_SerialPort */

#ifndef _Included_com_serialport_SerialPort
#define _Included_com_serialport_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_face_1chtj_base_1iotutils_serialport_SerialPort_open(JNIEnv *env, jclass clazz,
                                                              jstring path, jint baudrate,
                                                              jint flags);
/*
 * Class:     com_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL
Java_com_face_1chtj_base_1iotutils_serialport_SerialPort_open2(JNIEnv *env, jclass clazz,
                                                               jstring path, jint baudrate,
                                                               jint data_bits, jint stop_bits,
                                                               jchar parity);

/*
 * Class:     com_serialport_SerialPort
 * Method:    close
 * Signature: ()V
 */

JNIEXPORT void JNICALL
Java_com_face_1chtj_base_1iotutils_serialport_SerialPort_close(JNIEnv *env, jobject thiz);

#ifdef __cplusplus
}
#endif
#endif
