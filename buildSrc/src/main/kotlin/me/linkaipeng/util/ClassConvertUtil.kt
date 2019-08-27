package me.linkaipeng.util

import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import javassist.ClassPool
import javassist.CtClass
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

/**
 * Created by linkaipeng on 2019-08-02.
 *
 */
object ClassConvertUtil {

    fun convert(inputs: Collection<TransformInput>?, outputProvider: TransformOutputProvider?, pool: ClassPool): MutableList<CtClass> {

        val classNames = mutableListOf<String>()
        val allClass = mutableListOf<CtClass>()
        inputs?.map { transformInput ->

            transformInput.directoryInputs.map { directoryInput ->

                val dirPath = directoryInput.file.absolutePath

                pool.insertClassPath(dirPath)

                FileUtils.listFiles(directoryInput.file, null, true).map {
                    if (it.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
                        val className = it.absolutePath.substring(dirPath.length + 1,
                            it.absolutePath.length - SdkConstants.DOT_CLASS.length).replace('/', '.')

                        if(classNames.contains(className)){
                            throw RuntimeException("You have duplicate classes with the same name : $className please remove duplicate classes ")
                        }
                        classNames.add(className)
                    }

                }

                // 获取output目录
                val dest = outputProvider?.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                println(directoryInput.file.path + " ---> " + dest?.toPath())

                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }


            //遍历jar文件 对jar不操作，但是要输出到out路径
            transformInput.jarInputs.map { jarInput ->
                pool.insertClassPath(jarInput.file.absolutePath)
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

        classNames.map {
            try {
                allClass.add(pool.get(it))
            } catch (e: javassist.NotFoundException) {
                println("class not found exception class name:$it ")
            }
        }

        return allClass
    }
}