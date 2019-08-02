package com.test.task


open class HelloTask : org.gradle.api.DefaultTask() {

    @org.gradle.api.tasks.TaskAction
    fun hello() {
        println("hello ~")
//        CostTimeTransform()
    }
}