# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
# 设置CMake构造本地库所需要的最低版本
cmake_minimum_required(VERSION 3.4.1)

# 创建并命名库，可以将库的类型设置为静态
# 或共享，并提供该库源代码的相对路径。
# 你可以定义多个library库，并使用CMake来构建。
# Gradle会自动打包库集成到apk中。
add_library( # 库名：Sets the name of the library.
        native-lib

        # 库的分享类型：Sets the library as a shared library.
        SHARED

        # 库所在的相对路径：Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp)

add_library( # 库名：Sets the name of the library.
        jni_crash_demo

        # 库的分享类型：Sets the library as a shared library.
        SHARED

        # 库所在的相对路径：Provides a relative path to your source file(s).
        src/main/cpp/crash.cpp)
# NDK提供了一套实用的原生API和库，可以使用find_library搜索NDK中存在的库，只需要加入所需要使用库的名称即可，如下面的日志库log-lib。
# 更多的库可参考https://developer.android.com/ndk/guides/stable_apis
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# 指定CMake连接到目标库中，可以链接多个库，如上面的JNITest库以及NDK中自带的log-lib库
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
target_link_libraries( # Specifies the target library.
        jni_crash_demo

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

