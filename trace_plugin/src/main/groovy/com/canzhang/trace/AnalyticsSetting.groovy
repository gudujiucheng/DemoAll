package com.canzhang.trace

class AnalyticsSetting {
    static final String PLUGIN_NAME = "com.canzhang.bury_point"

    /**
     * 插桩对应的代码索引路径
     */
    static
    final String GENERATE_SDK_API_CLASS_PATH = 'com/canzhang/sample/manager/asm/dynamic_style/DataAutoTrackHelper'

    /**
     * 自定义注解对应的位置（注意后方拼接有字符）
     */
    static
    final String ON_CLICK_ANNOTATION_PATH = 'Lcom/canzhang/sample/manager/asm/dynamic_style//DataTrackViewOnClick;'


}