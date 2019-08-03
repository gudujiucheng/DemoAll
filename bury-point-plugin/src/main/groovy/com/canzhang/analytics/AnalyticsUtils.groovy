package com.canzhang.analytics
import org.objectweb.asm.Opcodes

class AnalyticsUtils implements Opcodes {
    static boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0
    }

    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0
    }

    static void logD(String tips) {
        if (AnalyticsExtension.isShowDebugInfo) {
            println(tips)
        }
    }
}
