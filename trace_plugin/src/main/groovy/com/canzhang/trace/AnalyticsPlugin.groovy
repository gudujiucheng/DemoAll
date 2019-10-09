package com.canzhang.trace
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 可以通过配置主工程目录中的gradle.properties 中的
 * fqlAnalytics.disablePlugin字段来控制是否开启此插件
 */
class AnalyticsPlugin implements Plugin<Project> {
    void apply(Project project) {

        AnalyticsExtension extension = project.extensions.create("slowMethodConfig", AnalyticsExtension)

        boolean disableAnalyticsPlugin = false
        Properties properties = new Properties()
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableAnalyticsPlugin = Boolean.parseBoolean(properties.getProperty("slowMethodConfig.disablePlugin", "false"))
        }

        if (!disableAnalyticsPlugin) {
            println("------------您开启了慢方法检测插桩插件--------------")
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            //注册我们的transform类
            appExtension.registerTransform(new AnalyticsTransform(project, extension))
        } else {
            println("------------您已关闭了慢方法检测插桩插件--------------")
        }
    }
}