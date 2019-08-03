package com.test.plugin

import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

/**
 * Created by linkaipeng on 2019-08-02.
 *
 */
class ClassConvertUtil {

    fun convert(inputs: Collection<TransformInput>?, outputProvider: TransformOutputProvider?, pool: ClassPool) {

        inputs?.map { transformInput ->

            transformInput.directoryInputs.map { directoryInput ->
                val dirPath = directoryInput.file.absolutePath
                pool.insertClassPath(dirPath)

                // 递归遍历所有文件
                FileUtils.listFiles(directoryInput.file, null, true).map {
                    if (it.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
                        val className = it.absolutePath.substring(dirPath.length + 1,
                            it.absolutePath.length - SdkConstants.DOT_CLASS.length).replace('/', '.')


                        if (className.contains("MainActivity")) {
                            val mainClass = pool.get(className)
                            //解冻
                            if (mainClass.isFrozen) {
                                mainClass.defrost()
                            }

                            val ctMethod = mainClass.getDeclaredMethod("test")
                            println("方法名 = $ctMethod")

                            addTimeCountMethod(ctMethod)
                            mainClass.writeFile(dirPath)
                            mainClass.detach()//释放
                        }

                    }

                }

                // 获取output目录
                val dest = outputProvider?.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                println(directoryInput.file.path + " ---> " + dest?.toPath())

                // 将 input 的目录复制到 output 指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }


            //遍历jar文件 对jar不操作，但是要输出到out路径
            transformInput.jarInputs.map { jarInput ->
                // 重命名输出文件（同目录copyFile会冲突）
                var jarName = jarInput.name
                val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length - 4)
                }
                val dest = outputProvider?.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }

    private fun addTimeCountMethod(method: CtMethod) {
        print("addTimeCountMethod   -------  ${method.name}")
        method.addLocalVariable("start", CtClass.longType)
        method.insertBefore("start = System.currentTimeMillis();")
        method.insertAfter("android.util.Log.d(\"MainTest\", \"cost time is :\" + (System.currentTimeMillis() - start) + \"ms\");")
        val toastCode = "android.widget.Toast.makeText(this,\"这里是 transform 过程插入的代码\", android.widget.Toast.LENGTH_SHORT).show();"
        method.insertAfter(toastCode)
    }
}