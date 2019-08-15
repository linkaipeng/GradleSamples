package com.seewo.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class MyHelloTask : DefaultTask() {

    @TaskAction
    fun hello() {
        println("hello MyHelloTask task ~")
    }
}