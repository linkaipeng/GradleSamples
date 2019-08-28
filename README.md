# 大纲

1. Gradle Task
2. 实战1：Gradle 在持续集成中的简单应用
3. Gradle Plugin
4. Gradle Transform
5. 实战2：Transform 撸一个方法耗时统计插件

> - Github: https://github.com/gradle/gradle
> - https://gradle.org/kotlin/


# Gradle Task

- Project API: https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html
- Task API: https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html
- TaskContainer API: https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/TaskContainer.html

## 实现 Task 的多种方式

### Build script 直接定义

task 直接写在 `build.gradle.kts` 文件里面
如：
```
tasks.register("buildScriptTask") {
    doLast {
        println("hello buildScript~")
    }
}
```

>执行：./gradlew -q buildScriptTask

> `-q` :  quiet. Log errors only.


### 定义在独立项目

build script 中直接定义的方式，作用范围仅仅是在当前的 `build.gradle.kts` 文件中，那么如果有的 task 希望被多个构建脚本所应用，那么可以将其放在独立的项目中，再由  `build.gradle.kts` 引用。

1、继承 DefaultTask，使用注解 **@TaskAction** 指定任务执行的方法

```
open class MyHelloTask : DefaultTask() {

    @TaskAction
    fun hello() {
        println("hello MyHelloTask task ~")
    }
}
```

2、需要使用到的项目进行依赖，（后面 plugin 会讲到，这里先不讲）

3、项目  `build.gradle.kts` 中引用

```
import me.linkaipeng.task.MyHelloTask

...

tasks.register<MyHelloTask>("myHelloTask")
```

> 执行 ./gradlew -q myHelloTask

## Task 获取参数

### 配置文件 `gradle.properties` 获取

`gradle.properties` 中加入

```
me.linkaipeng.propertie.hi=hi hi
```

获取代码

```
val propFromFile = project.properties["me.linkaipeng.propertie.hi"]
println("propFromFile = $propFromFile")
```

### 命令行获取

在执行任务的命令行后面加上 -Pxxx，比如

```
./gradlew -q readPropertiesTask -Phi2=tete
```

那么就输入了一个名为 **hi2** 的参数；

如果要输入多个参数，那么

```
./gradlew -q readPropertiesTask -Phi2=tete -Phi3=rr
```


## Task 执行顺序控制

### dependsOn

```
task("developTask") {
    group = "software"
    description = "develop software."
    doLast {
        println("develop")
    }
}

task("compileTask") {
    group = "software"
    dependsOn("developTask")
    doLast {
        println("compile")
    }
}
```

比如，编译任务要依赖开发任务，那么可以用 `dependsOn("developTask")` 指定依赖关系；

> 执行 ./gradlew -q compileTask

结果：

```
develop
compile
```

### mustRunAfter

```
task("developTask") {
    group = "software"
    description = "develop software."
    doLast {
        println("develop")
    }
}

task("compileTask") {
    group = "software"
    mustRunAfter("developTask")
    doLast {
        println("compile")
    }
}

task("testingTask") {
    group = "software"
    dependsOn("developTask")
    dependsOn("compileTask")
    doLast {
        println("testing")
    }
}
```

比如，执行测试任务的时候，需要依赖到开发和编译任务，但是必须要求开发任务在编译任务之前，那么可以通过`mustRunAfter("developTask")`指定

> 执行 ./gradlew -q testingTask

**假如没有指定的话，那么会按照字母顺序执行，结果为：**

```
compile
develop
testing
```

**有指定编译任务必须在开发之后执行，那么结果则为：**

```
develop
compile
testing
```

### finalizedBy

指定任务执行之后，最后执行的任务，比如，测试任务完成后进行打包操作

现在增加打包任务

```
task("packagingTask") {
    group = "software"
    doLast {
        println("packaging")
    }
}
```

然后在测试任务中指定

```
task("testingTask") {
    group = "software"
    dependsOn("developTask")
    dependsOn("compileTask")
    finalizedBy("packagingTask")
    doLast {
        println("testing")
    }
}
```

> 执行 ./gradlew -q testingTask


结果：
```
develop
compile
testing
packaging
```

### shouldRunAfter

类似 `mustRunAfter` ，优先级 `mustRunAfter` 更高


## 跳过任务（Skip Task）

顾名思义，就是条件性的不执行某个任务

### onlyIf

类似条件语句，只有满足当前条件，则执行

```
val skipDemoTask by tasks.registering {
    doLast {
        println("skipDemoTask ~")
    }
}

skipDemoTask {
    onlyIf { !project.hasProperty("skip") }
}
```

> 不跳过，执行  ./gradlew -q skipDemoTask

当检测到参数 `skip`，则跳过

> 跳过，执行 ./gradlew -q skipDemoTask -Pskip


### StopExecutionException

借助异常停止执行

```
val exceptionDemoTask by tasks.registering {
    doLast {
        if (project.hasProperty("exception")) {
            throw StopExecutionException()
        }
        println("exceptionDemoTask ~")
    }
}
```

> ./gradlew -q exceptionDemoTask -Pexception

### enabled 值

通过控制 task 的 `enabled`，达到跳过效果，**false** 的时候，不执行

```
val enableDemoTask by tasks.registering {
    doLast {
        println("enableDemoTask ~")
    }
}
enableDemoTask {
    enabled = false
}
```

> 执行 ./gradlew -q enableDemoTask


## 实战：自定义参数 + buildConfigField 实现动态更改环境

可以在构建命令后面使用 `-P` 传入命令

传入参数：

```
./gradlew -q testTask -Phost=test.test.com
```

获取参数：

```
project.properties.keys.map {
    if("host" == it) {
        println("propertie key = $it, value = ${project.properties[it]}")
    }
}
```


# Gradle Plugin
> 官方文档： https://docs.gradle.org/current/userguide/custom_plugins.html

## 实现 Plugin 的三种方式

### Build script

插件直接写在 `build.gradle.kts` 文件里面
如：
```
class TestPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.task("testTask") {
            doLast {
                print("This is testTask in TestPlugin.")
            }
        }
    }
}

apply<TestPlugin>()
```

> 执行：./gradlew -q testTask



### buildSrc 项目

`buildSrc` 以独立 module 存在，位置和目录结构如下：

```
.
├── build.gradle.kts
├── buildSrc
│   ├── build.gradle.kts
│   └── src
│       └── main
│           └── kotlin
└── settings.gradle.kts
```


- `buildSrc/build.gradle.kts`

在 build.gradle.kts 中声明插件的 name、id 等信息

```
gradlePlugin {
    plugins {
        create("PrintMethodNamePlugin") {
            id = "print-method-plugin"
            implementationClass = "com.test.plugin.PrintMethodNamePlugin"
        }
    }
}
```

这样项目中就可以使用：

```
plugins {
    id("com.android.application")
    ...
    id("print-method-plugin")
}
```


> 执行：./gradlew transformClassesWithPrintMethodNameTransformForDebug 

### 独立项目

```
costTimePlugin
.
├── build.gradle.kts
├── buildSrc
│   ├── build.gradle.kts
│   └── src
│       └── main
│           └── kotlin
│           └── resources
│                └── META-INF.gradle-plugins
│                     └── me.linkaipeng.cost-time.properties
└── settings.gradle.kts
```

- `resources/META-INF.gradle-plugins/me.linkaipeng.cost-time.properties`
  
必须存在，是为了声明插件让项目识别到，内容如下：

```
implementation-class=me.linkaipeng.plugin.CostTimeStandAlongPlugin
```

- 传到本地仓库/远程仓库

`build.gradle.kts` 文件声明（详细见源代码）：
```
plugins {
    ...
    `maven-publish`
}

group = "me.linkaipeng.plugin"
version = "1.0.3"

... 

publishing {
    repositories {
        maven {
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

```

工程 `build.gradle.kts` 配置本地 repo 路径和依赖：

```
repositories {
    maven {
        url = uri("file:///Users/linkaipeng/Documents/demos/GradleKotlinDemo/repo/")
    }
}

dependencies {
    ...
    classpath("me.linkaipeng.plugin:costTimePlugin:1.0.3")
}

```

这样项目中就可以使用：

```
plugins {
    id("com.android.application")
    ...
    id("me.linkaipeng.cost-time")
}
```
> id 即是 刚刚定义的 properties 文件的文件名

> 执行：./gradlew transformClassesWithCostTimeStandAlongTransformForDebug



### 如何选择使用哪种方式创建

#### Build script
> You can include the source for the plugin directly in the build script. This has the benefit that the plugin is automatically compiled and included in the classpath of the build script without you having to do anything. However, the plugin is not visible outside the build script, and so you cannot reuse the plugin outside the build script it is defined in.

- 编写简单，无须配置
- 外部不可用到


#### `buildSrc` project
> You can put the source for the plugin in the rootProjectDir/buildSrc/src/main/groovy directory (or rootProjectDir/buildSrc/src/main/java or rootProjectDir/buildSrc/src/main/kotlin depending on which language you prefer). Gradle will take care of compiling and testing the plugin and making it available on the classpath of the build script. The plugin is visible to every build script used by the build. However, it is not visible outside the build, and so you cannot reuse the plugin outside the build it is defined in.

- 项目内可见
- 适用于逻辑较为复杂的插件
- 外部无法使用

#### Standalone project
> You can create a separate project for your plugin. This project produces and publishes a JAR which you can then use in multiple builds and share with others. Generally, this JAR might include some plugins, or bundle several related task classes into a single library. Or some combination of the two.

- 适合较为复杂的场景
- 可以独立打包发布


### Gradle Transform

- getName()
> 用来定义 transform 任务的名称。

- getInputTypes()
> 用来限定这个 transform 能处理的文件类型，一般来说我们要处理的都是 class 文件，就返回 TransformManager.CONTENT_CLASS,当然如果你是想要处理资源文件，可以使用TransformManager.CONTENT_RESOURCES 。

- getScopes()
> 指定的的就是哪些文件了。比如说我们如果想处理 class 文件，但 class 文件可以是当前module的，也可以是子 module 的，还可以是第三方 jar 包中的.


Type | 作用域
---|---
PROJECT | 只处理当前项目
SUB_PROJECTS | 只处理子项目
PROJECT_LOCAL_DEPS |只处理当前项目的本地依赖,例如 jar, aar
SUB_PROJECTS_LOCAL_DEPS |只处理子项目的本地依赖,例如 jar, aar
EXTERNAL_LIBRARIES | 只处理外部的依赖库
PROVIDED_ONLY | 只处理本地或远程以provided形式引入的依赖库
TESTED_CODE | 测试代码



- inIncremental()
> 是否支持增量编译。

- transform()
> 获取输入的 class 文件，然后做些修改，最后输出修改后的 class 文件。

### transform 步骤
- 获取输入文件
> 一种是本 module 自己的 src下的源码编译后的 class 文件，一种是第三方的 jar 包文件，我们需要分开单独处理。

- 获取输出路径
> 输入文件有了，我们要先确定输出路径，这里要注意，输出路径必须用特殊方式获取，而不能自己随意指定，否则下一个任务就无法获取你这次的输出文件了，编译失败。

- 处理输入文件
> 对输入的文件进行处理，也是核心的步骤；做代码插入、修改等操作就是在这个步骤进行操作。

### 演示 DEMO


# 其他
## 使用 kotlin 来写 Gradle 脚本
- 官方迁移文档： https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/#before_you_start_migrating
- 


