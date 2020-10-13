package com.canzhang.sample.manager.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.canzhang.sample.R

class KotlinTestActivity : AppCompatActivity() {//继承
    //复写
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)
    }
}