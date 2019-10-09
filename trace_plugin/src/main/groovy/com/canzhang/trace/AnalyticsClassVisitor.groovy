package com.canzhang.trace

import org.objectweb.asm.*

/**
 * 使用ASM的ClassReader类读取.class的字节数据，并加载类，
 * 然后用自定义的ClassVisitor，进行修改符合特定条件的方法，
 * 最后返回修改后的字节数组
 */
class AnalyticsClassVisitor extends ClassVisitor implements Opcodes {

//插入的外部类具体路径
    private String[] mInterfaces
    private ClassVisitor classVisitor
    private String mCurrentClassName

    AnalyticsClassVisitor(final ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor)
        this.classVisitor = classVisitor
    }

    private
    static void visitMethodWithLoadedParams(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes) {
        for (int i = start; i < start + count; i++) {
            methodVisitor.visitVarInsn(paramOpcodes[i - start], i)
        }
        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false)
    }

    /**
     * 这里可以拿到关于.class的所有信息，比如当前类所实现的接口类表等
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        mInterfaces = interfaces
        mCurrentClassName = name
    }

    /**
     * 这里可以拿到关于method的所有信息，比如方法名，方法的参数描述等
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        String nameDesc = name + desc

        methodVisitor = new AnalyticsDefaultMethodVisitor(methodVisitor, access, name, desc) {

            @Override
            void visitEnd() {
                super.visitEnd()
            }

            @Override
            void visitInvokeDynamicInsn(String name1, String desc1, Handle bsm, Object... bsmArgs) {
                super.visitInvokeDynamicInsn(name1, desc1, bsm, bsmArgs)
            }

            @Override
            protected void onMethodExit(int opcode) {//方法退出节点
                super.onMethodExit(opcode)

                mv.visitLdcInsn(name);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                mv.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH,  "setEndTime", "(Ljava/lang/String;J)V", false);

                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitLdcInsn(name);
                mv.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "getCostTime", "(Ljava/lang/String;)Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }

            @Override
            protected void onMethodEnter() {//方法进入节点
                super.onMethodEnter()

                AnalyticsUtils.logD("方法进入 nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)

                mv.visitLdcInsn(name);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                mv.visitMethodInsn(INVOKESTATIC,  AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "setStartTime", "(Ljava/lang/String;J)V", false);



            }

            @Override
            AnnotationVisitor visitAnnotation(String s, boolean b) {
                return super.visitAnnotation(s, b)
            }
        }
        return methodVisitor
    }
}