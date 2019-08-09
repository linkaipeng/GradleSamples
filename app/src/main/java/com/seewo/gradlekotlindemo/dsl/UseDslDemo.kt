package com.seewo.gradlekotlindemo.dsl

/**
 * Created by linkaipeng on 2019-08-03.
 *
 */
class UseDslDemo {

    fun test() {

        likeAndroid {
            compileSdkVersion(29)
            defaultConfig {
                applicationId = "cpm.seewo.linkaipeng"
                versionCode = 100
                versionName = "1.0.1"
            }
        }

    }
}