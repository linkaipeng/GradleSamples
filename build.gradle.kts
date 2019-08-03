// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version = "1.3.31"
    repositories {
        maven {
            url = uri("file:///Users/linkaipeng/Documents/demos/GradleKotlinDemo/repo/")
        }
        google()
        jcenter()
        
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.seewo.plugin:costTimePlugin:1.0.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {

        maven {
            url = uri("file:///Users/linkaipeng/Documents/demos/GradleKotlinDemo/repo/")
        }
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}