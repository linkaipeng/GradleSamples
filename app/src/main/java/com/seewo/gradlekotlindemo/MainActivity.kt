package com.seewo.gradlekotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seewo.annotation.CostTime
import com.seewo.gradlekotlindemo.dsl.UseDslDemo


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test("")
        UseDslDemo().test()
    }

    @CostTime
    fun test(testStr: String) {
        Thread.sleep(1000)
    }

    fun testCostTime() {
        Thread.sleep(1000)
    }

}
