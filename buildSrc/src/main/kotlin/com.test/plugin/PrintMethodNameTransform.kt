package com.test.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.seewo.gradlekotlindemo.com.test.util.ClassConvertUtil
import javassist.ClassPool
import org.gradle.api.Project


/**
 * Created by linkaipeng on 2019-07-28.
 *
 */
class PrintMethodNameTransform(project: Project): Transform() {

    private var mProject = project

    override fun transform(transformInvocation: TransformInvocation?) {
        println("-------- PrintMethodNameTransform ---------")
        super.transform(transformInvocation)
        val pool = ClassPool.getDefault()

        val allClass = ClassConvertUtil.convert(transformInvocation?.inputs, transformInvocation?.outputProvider, pool)

        allClass.map { ctClass ->

            ctClass.declaredMethods.map {
                println("method name ===== ${it.name}")
            }
        }

        println("allClass size = ${allClass.size}")

    }

    override fun getName(): String {
        return "PrintMethodNameTransform"
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