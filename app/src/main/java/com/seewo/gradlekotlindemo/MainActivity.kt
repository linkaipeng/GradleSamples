package com.seewo.gradlekotlindemo

import android.os.Bundle
import android.widget.Toast
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
        Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
    }

    fun testCostTime() {
        Thread.sleep(1000)
    }

}
