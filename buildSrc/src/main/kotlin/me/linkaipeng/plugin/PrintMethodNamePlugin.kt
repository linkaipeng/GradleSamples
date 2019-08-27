package me.linkaipeng.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by linkaipeng on 2019-07-28.
 *
 */
class PrintMethodNamePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(" ----- buildSrc PrintMethodName plugin ---- ")
        val android = target.extensions.getByType(AppExtension::class.java)
        android.registerTransform(PrintMethodNameTransform(target))
    }
}
