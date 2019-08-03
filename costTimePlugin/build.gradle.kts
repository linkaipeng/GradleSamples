
plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "com.seewo.plugin"
version = "1.0.3"

repositories {
    mavenCentral()
}


//uploadArchives{ //当前项目可以发布到本地文件夹中
//    repositories {
//        mavenDeployer {
//            repository(url: uri('../repo')) //定义本地maven仓库的地址
//        }
//    }
//}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compile(gradleApi())
    compile(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:3.4.2")
    implementation("com.seewo.annotation:annotationLib:1.0.0")
}

publishing {
    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            url = uri("../repo")
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}

