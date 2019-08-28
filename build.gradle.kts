// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val repoPath = File("repo").absolutePath
    val kotlin_version = "1.3.31"
    repositories {
        maven {
            url = uri("file://$repoPath/")
        }
        google()
        jcenter()
        
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("me.linkaipeng.plugin:costTimePlugin:1.0.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {

    val repoPath = File("repo").absolutePath
    repositories {

        maven {
            url = uri("file://$repoPath/")
        }
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}