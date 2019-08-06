package com.test.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by linkaipeng on 2019-07-28.
 *
 */
class CostTimePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(" ----- buildSrc plugin ---- ")
        val android = target.extensions.getByType(AppExtension::class.java)
        android.registerTransform(CostTimeTransform(target))
    }
}
