package com.canzhang.sample.manager.log

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.InvocationTargetException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

private val sNormalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)

object Util {

    @JvmStatic
    fun getNormalFormatTimeStr(timeMills: Long?): String? {
        timeMills?.let {
            return sNormalFormat.format(it)
        }
        return null
    }


    private var sApp: Application? = null

    fun init(app: Application?) {
        sApp = app
    }

    /**
     * Return the Application object.
     *
     * Main process get app by UtilsFileProvider,
     * and other process get app by reflect.
     *
     * @return the Application object
     */
    @JvmStatic
    fun getApp(): Application? {
        if (sApp != null) {
            return sApp
        }
        init(getApplicationByReflect())
        if (sApp == null) {
            throw NullPointerException("please init mati first")
        }
        return sApp
    }


    private fun getApplicationByReflect(): Application? {
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val thread = getActivityThread()
            val app = activityThreadClass.getMethod("getApplication").invoke(thread) ?: return null
            return app as Application
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


    private fun getActivityThread(): Any? {
        val activityThread = getActivityThreadInActivityThreadStaticField()
        return activityThread ?: getActivityThreadInActivityThreadStaticMethod()
    }

    private fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThreadField =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            sCurrentActivityThreadField.isAccessible = true
            sCurrentActivityThreadField[null]
        } catch (e: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticField: " + e.message
            )
            null
        }
    }

    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null)
        } catch (e: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticMethod: " + e.message
            )
            null
        }
    }

    /**
     * 上报到机器人
     */
    fun reportToBot(botUrl: String?, msg: String?) {
        if (botUrl == null || msg == null) {
            return
        }
        try {
            val url =
                URL(botUrl)
            val connection = url
                .openConnection() as HttpURLConnection
            connection.readTimeout = 2000
            connection.connectTimeout = 2000
            connection.useCaches = false
            connection.doInput = true
            connection.doOutput = true
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")


            val dos = DataOutputStream(
                connection
                    .outputStream
            )
            val json = JSONObject()
            json.put("msgtype", "text")
            val jsonContent = JSONObject()
            var content = msg
            if (content.length > 4000) {
                content = content.substring(0, 4000)
                content = "$content\n.....堆栈过长被省略，请去app查看完整日志！"
            }
            jsonContent.put("content", content)
            json.put("text", jsonContent)
            dos.write(json.toString().toByteArray())
            dos.flush()
            dos.close()
            val responseCode = connection.responseCode
            val resultIs = connection.inputStream
            val resultStr = streamToString(resultIs)
            connection.disconnect()
//            Log.e(BlockManager.TAG, "bot responseCode $responseCode resultStr:$resultStr")
        } catch (e: Exception) {
//            Log.e(BlockManager.TAG, Log.getStackTraceString(e))
        }
    }


    private fun streamToString(inputStream: InputStream): String? {
        try {
            val bos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = 0
            while (inputStream.read(buffer).also { len = it } != -1) {
                bos.write(buffer, 0, len)
            }
            inputStream.close()
            bos.close()
            return String(bos.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    fun getExtraInfo(): LinkedHashMap<String, String> {
        val mPackageManager = getApp()?.packageManager
        var mPackageInfo: PackageInfo? = null
        try {
            mPackageInfo = getApp()?.packageName?.let {
                mPackageManager?.getPackageInfo(
                    it, PackageManager.GET_ACTIVITIES
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val extraInfo = LinkedHashMap<String, String>()
        var appName = ""
        mPackageInfo?.packageName?.let {
            val applicationInfo =
                mPackageManager?.getApplicationInfo(it, 0)
            applicationInfo?.let {
                appName = applicationInfo.loadLabel(mPackageManager).toString()
            }
        }
        extraInfo["应用名称"] = "" + appName
        extraInfo["应用包名"] = "" + mPackageInfo?.packageName
        extraInfo["版本名"] = mPackageInfo?.versionName ?: ""
        extraInfo["版本号"] = "" + mPackageInfo?.versionCode
        //尝试获取用户设备名称
        var name =
            Settings.System.getString(getApp()?.contentResolver, "device_name")
        if (TextUtils.isEmpty(name) || name.equals(Build.MODEL)) {
            name = Settings.System.getString(getApp()?.contentResolver, "bluetooth_name")
        }
        if (TextUtils.isEmpty(name)) {
            name = Settings.Secure.getString(getApp()?.contentResolver, "bluetooth_name")
        }
        if (name == null) {
            name = ""
        }
        extraInfo["设备信息"] = "${Build.BRAND} ${Build.MODEL} $name "
        extraInfo["系统版本"] = "" + Build.VERSION.SDK_INT

        extraInfo["android_id"] =
            Settings.System.getString(getApp()?.contentResolver, Settings.Secure.ANDROID_ID)
        return extraInfo
    }


    fun getTips(time: String?, str: String) =
        ("$time<font color=\"#F61606\"> 发生了$str</font>"
                + "<font color=\"#023EF4\"> 点击查看详情</font>")

}