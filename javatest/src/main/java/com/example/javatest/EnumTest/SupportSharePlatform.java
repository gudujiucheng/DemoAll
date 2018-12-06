package com.example.javatest.EnumTest;

/**
 * 当前分享所有支持的平台  2018年12月6日20:34:40  by zc
 */
public enum SupportSharePlatform {
    SINA_WEIBO("1"), SHORT_MESSAGE("2"), Q_ZONE("3"), WECHAT("4"), WECHAT_MOMENTS("5"), COPY_LINK("6"), S_QQ("7"), SHARE_PIC("c"), WECHAT_MINI_PROGRAM("d");
    public static final String ALL_SUPPORT_PLATFORM = getAllPlatform();
    private String value;

    /**
     * 构造函数
     *
     * @param value
     */
    SupportSharePlatform(String value) {
        this.value = value;
    }

    /**
     * 获取属性值的方法
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    public static String getAllPlatform() {
        SupportSharePlatform[] values = SupportSharePlatform.values();
        StringBuilder builder = new StringBuilder();
        for (SupportSharePlatform platform : values) {
            builder.append(platform.getValue());
        }
        return builder.toString();
    }
}
