package me.linkaipeng.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CostTimeStandAlongPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val android = target.extensions.getByType(AppExtension::class.java)
        android.registerTransform(CostTimeStandAlongTransform(target))
    }

}