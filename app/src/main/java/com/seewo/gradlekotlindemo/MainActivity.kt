package com.seewo.gradlekotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test("")
    }

    @Test2
    fun test(testStr: String) {
        Thread.sleep(1000)
    }

    fun testCostTime() {
        Thread.sleep(1000)
    }

}
