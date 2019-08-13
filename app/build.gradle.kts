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