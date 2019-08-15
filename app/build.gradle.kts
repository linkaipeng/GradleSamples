import com.seewo.task.MyHelloTask

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
//    id("cost-time-plugin")
    id("com.seewo.cost-time")
}


android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.seewo.gradlekotlindemo"
        minSdkVersion(19)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release")  {
            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.31")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("com.seewo.annotation:annotationLib:1.0.0")


}

tasks.register("buildScriptTask") {
    description = "直接定义的 task"
    doLast {
        println("hello buildScript~")
    }
}


class TestPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.task("testTask") {
            doLast {

                project.properties.keys.map {
                    if("myName" == it) {
                        println("propertie key = $it, value = ${project.properties[it]}")
                    }
                }

                println("This is testTask in TestPlugin.")
            }
        }
    }

}

apply<TestPlugin>()




// 任务顺序 demo

task("developTask") {
    group = "software"
    description = "develop software."
    doLast {
        println("develop")
    }
}

task("compileTask") {
    group = "software"
//    dependsOn("developTask")
    mustRunAfter("developTask")
    doLast {
        println("compile")
    }
}

task("testingTask") {
    group = "software"
    dependsOn("developTask")
    dependsOn("compileTask")
    finalizedBy("packagingTask")
    doLast {
        println("testing")
    }
}

task("packagingTask") {
    group = "software"
    doLast {
        println("packaging")
    }
}

// read params demo

task("readPropertiesTask") {
    doLast {
        val propFromFile = project.properties["com.seewo.propertie.hi"]
        println("propFromFile = $propFromFile")

        if(project.hasProperty("hi2")) {
            val propFromCommandLine = project.properties["hi2"]
            println("propFromCommandLine = $propFromCommandLine")
        }

        if(project.hasProperty("hi3")) {
            val propFromCommandLine = project.properties["hi3"]
            println("propFromCommandLine = $propFromCommandLine")
        }
    }
}


// skip task demo

val skipDemoTask by tasks.registering {
    doLast {
        println("skipDemoTask ~")
    }
}

skipDemoTask {
    onlyIf { !project.hasProperty("skip") }
}



val exceptionDemoTask by tasks.registering {
    doLast {
        if (project.hasProperty("exception")) {
            throw StopExecutionException()
        }
        println("exceptionDemoTask ~")
    }
}


val enableDemoTask by tasks.registering {
    doLast {
        println("enableDemoTask ~")
    }
}
enableDemoTask {
    enabled = false
}



tasks.register<MyHelloTask>("myHelloTask")