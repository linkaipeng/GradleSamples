package com.seewo.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.test.plugin.ClassConvertUtil
import javassist.ClassPool
import org.gradle.api.Project


/**
 * Created by linkaipeng on 2019-07-28.
 *
 */
class CostTimeStandAlongTransform(project: Project): Transform() {

    private var mProject = project

    override fun transform(transformInvocation: TransformInvocation?) {
        println("-------- transform ---------")
        super.transform(transformInvocation)
        val pool = ClassPool.getDefault()

        // 添加 android 依赖
        val android = mProject.extensions.getByType(AppExtension::class.java)
        pool.appendClassPath(android.bootClasspath[0].toString())

        ClassConvertUtil().convert(transformInvocation?.inputs, transformInvocation?.outputProvider, pool)
    }


    override fun getName(): String {
        return "CostTimeStandAlongTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }
}