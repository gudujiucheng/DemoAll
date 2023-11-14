package com.example.administrator.demoall.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.text.TextUtils


class MyContentProvider : ContentProvider() {
    private var sharedPreferences: SharedPreferences? = null
    private var uriMatcher: UriMatcher? = null
    override fun onCreate(): Boolean {
        sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //路径识别器
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        //路径匹配规则，可以添加多个
        //例如：content://com.tencent.replayHelper.provider.PersonProvider/ACTION_HELP_JSON,返回值就是(ACTION_HELP_JSON_CODE)=1
        uriMatcher?.addURI(AUTHORITY, ACTION_HELP_JSON, ACTION_HELP_JSON_CODE)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val type = uriMatcher?.match(uri)
        //根据类型返回对应数据
        return if (type == ACTION_HELP_JSON_CODE) {
            val cursor = MatrixCursor(arrayOf(ACTION_HELP_JSON))
            val myString = sharedPreferences?.getString(ACTION_HELP_JSON, null)
            if (myString != null) {
                cursor.addRow(arrayOf<Any>(myString))
            }
            cursor
        } else {
            return null
        }
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val type = uriMatcher?.match(uri)
        return if (type == ACTION_HELP_JSON_CODE) {
            val myString = values?.getAsString(ACTION_HELP_JSON)
            if (!TextUtils.isEmpty(myString)) {
                val editor = sharedPreferences?.edit()
                editor?.putString(ACTION_HELP_JSON, myString)
                editor?.apply()
                context?.contentResolver?.notifyChange(uri, null)
                uri
            } else {
                null
            }
        } else {
            null
        }
    }


    override fun getType(uri: Uri): String? {
        val type = uriMatcher?.match(uri)
        return if (type == ACTION_HELP_JSON_CODE) {
            "vnd.android.cursor.item/vnd.$AUTHORITY.$ACTION_HELP_JSON"
        } else {
            return null
        }
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val type = uriMatcher?.match(uri)
        return if (type == ACTION_HELP_JSON_CODE) {
            val editor = sharedPreferences?.edit()
            editor?.remove(ACTION_HELP_JSON)
            editor?.apply()
            context?.contentResolver?.notifyChange(uri, null)
            1
        } else {
            return -1
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val type = uriMatcher?.match(uri)
        return if (type == ACTION_HELP_JSON_CODE) {
            val myString = values?.getAsString(ACTION_HELP_JSON)
            if (!TextUtils.isEmpty(myString)) {
                val editor = sharedPreferences?.edit()
                editor?.putString(ACTION_HELP_JSON, myString)
                editor?.apply()
                context?.contentResolver?.notifyChange(uri, null)
                1
            } else {
                -1
            }
        } else {
            -1
        }
    }

    companion object {
        private const val AUTHORITY = "com.tencent.replayHelper.providerx"
        private const val ACTION_HELP_JSON = "ACTION_HELP_JSON"
        private const val ACTION_HELP_JSON_CODE = 1
    }
}