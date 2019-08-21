plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

gradlePlugin {
    plugins {
        create("PrintMethodNamePlugin") {
            id = "print-method-plugin"
            implementationClass = "com.test.plugin.PrintMethodNamePlugin"
        }
    }
}

dependencies {
    compile(gradleApi())
    compile(localGroovy())
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.android.tools.build:gradle:3.4.2")
}