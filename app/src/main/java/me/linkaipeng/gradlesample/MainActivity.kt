package me.linkaipeng.gradlesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.linkaipeng.annotation.CostTime


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test("")
    }

    @CostTime
    fun test(testStr: String) {
        Thread.sleep(2000)
    }

}
