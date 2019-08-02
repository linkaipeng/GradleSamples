plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

gradlePlugin {
    plugins {
        create("CostTimePlugin") {
            id = "cost-time-plugin"
            implementationClass = "com.test.plugin.CostTimePlugin"
        }
    }
}

dependencies {
    compile(gradleApi())
    compile(localGroovy())
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.android.tools.build:gradle:3.4.2")
}