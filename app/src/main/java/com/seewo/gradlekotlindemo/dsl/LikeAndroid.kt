package com.seewo.gradlekotlindemo.dsl

import android.util.Log

/**
 * Created by linkaipeng on 2019-08-05.
 *
 */
class LikeAndroid {

    var compileSdkVersion: String? = null

    fun compileSdkVersion(apiLevel: Int) {
        compileSdkVersion("android-$apiLevel")
    }

    fun compileSdkVersion(apiLevel: String) {
        compileSdkVersion = apiLevel
    }

    fun defaultConfig(init: Config.() -> Unit) {
        val config = Config()
        config.init()

        Log.d("LikeAndroid", "config = ${config.applicationId}")
    }

}


fun likeAndroid(init: LikeAndroid.() -> Unit) {
    val likeAndroid = LikeAndroid()
    likeAndroid.init()

    Log.d("LikeAndroid", "compileSdkVersion = ${likeAndroid.compileSdkVersion}")
}