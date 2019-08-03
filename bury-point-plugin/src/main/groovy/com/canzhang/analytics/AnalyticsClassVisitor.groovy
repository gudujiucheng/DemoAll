package com.canzhang.analytics

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
            boolean isDataTrackViewOnClickAnnotation = false

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
            }

            @Override
            protected void onMethodEnter() {//方法进入节点
                super.onMethodEnter()

                if (isDataTrackViewOnClickAnnotation) {//注解
                    if (desc == '(Landroid/view/View;)V') {
                        AnalyticsUtils.logD("插桩：自定义注解类方法插入 nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/view/View;)V", false)
                        return
                    }
                }
                if ('android/support/design/widget/TabLayout' == mCurrentClassName) {//tablayout
                    if ('selectTab(Landroid/support/design/widget/TabLayout$Tab;)V' == nameDesc) {
                        AnalyticsUtils.logD("插桩：tabLayout nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackTabOnSelect", '(Landroid/support/design/widget/TabLayout$Tab;)V', false)
                    }
                }

                if ((mInterfaces != null && mInterfaces.length > 0)) {
                    if ((mInterfaces.contains('android/view/View$OnClickListener') && nameDesc == 'onClick(Landroid/view/View;)V')) {
                        AnalyticsUtils.logD("插桩：OnClickListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/view/View;)V", false)
                    } else if (mInterfaces.contains('android/content/DialogInterface$OnClickListener') && nameDesc == 'onClick(Landroid/content/DialogInterface;I)V') {
                        AnalyticsUtils.logD("插桩：DialogInterface.OnClickListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitVarInsn(ILOAD, 2)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/content/DialogInterface;I)V", false)
                    } else if (mInterfaces.contains('android/content/DialogInterface$OnMultiChoiceClickListener') && nameDesc == 'onClick(Landroid/content/DialogInterface;IZ)V') {
                        AnalyticsUtils.logD("插桩：DialogInterface.OnMultiChoiceClickListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitVarInsn(ILOAD, 2)
                        methodVisitor.visitVarInsn(ILOAD, 3)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/content/DialogInterface;IZ)V", false)
                    } else if (mInterfaces.contains('android/widget/CompoundButton$OnCheckedChangeListener') && nameDesc == 'onCheckedChanged(Landroid/widget/CompoundButton;Z)V') {
                        AnalyticsUtils.logD("插桩：CompoundButton.OnCheckedChangeListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitVarInsn(ILOAD, 2)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/widget/CompoundButton;Z)V", false)
                    } else if (mInterfaces.contains('android/widget/AdapterView$OnItemSelectedListener') && nameDesc == 'onItemSelected(Landroid/widget/AdapterView;Landroid/view/View;IJ)V') {
                        AnalyticsUtils.logD("插桩：AdapterView.OnItemSelectedListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitVarInsn(ALOAD, 2)
                        methodVisitor.visitVarInsn(ILOAD, 3)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/widget/AdapterView;Landroid/view/View;I)V", false)
                    } else if (mInterfaces.contains('android/widget/AdapterView$OnItemClickListener') && nameDesc == 'onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V') {
                        AnalyticsUtils.logD("插桩：AdapterView.OnItemClickListener nameDesc:" + nameDesc + " currentClassName:" + mCurrentClassName)
                        methodVisitor.visitVarInsn(ALOAD, 1)
                        methodVisitor.visitVarInsn(ALOAD, 2)
                        methodVisitor.visitVarInsn(ILOAD, 3)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, AnalyticsSetting.GENERATE_SDK_API_CLASS_PATH, "trackViewOnClick", "(Landroid/widget/AdapterView;Landroid/view/View;I)V", false)
                    }
                }
            }

            @Override
            AnnotationVisitor visitAnnotation(String s, boolean b) {
                /**
                 * 针对布局内定义android：onClick事件的，要在对应的方法上面加上注解才能操作到
                 */
                if (s == AnalyticsSetting.ON_CLICK_ANNOTATION_PATH) {
                    isDataTrackViewOnClickAnnotation = true
                }

                return super.visitAnnotation(s, b)
            }
        }
        return methodVisitor
    }
}