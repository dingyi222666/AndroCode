package io.dingyi222666.androcode.api

import android.util.Log
import io.dingyi222666.androcode.annotation.AutoService
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service


class AndroCodeContext : Context("root") {
    init {
        configureBase()
    }
}

fun Context.readServiceClasses(targetClassName: String, classLoader: ClassLoader) {
    val path = "META-INF/services/$targetClassName"

    val content = javaClass.classLoader?.getResourceAsStream(path)?.use {
        it.bufferedReader().readText()
    } ?: return


    for (line in content.split("\n")) {
        if (line.trim().isEmpty()) {
            continue
        }

        val classWithFunctionName = line.trim()

        val className = classWithFunctionName.substringBeforeLast(".")
        val functionName = classWithFunctionName.substringAfterLast(".")

        kotlin.runCatching {
            val clazz = classLoader.loadClass(className)

            val method = clazz.getMethod(functionName, Context::class.java)


            if (!method.isAnnotationPresent(AutoService::class.java)) {
                return@runCatching
            }

            val annotation = method.getAnnotation(AutoService::class.java) ?: return@runCatching
            println(annotation)
            registerConstructor(annotation.id) { ctx ->
                method.invoke(null, ctx) as Service
            }
        }.onFailure {
            Log.e("error", it.message, it)
        }

    }
}

fun Context.configureBase(classLoader: ClassLoader = this::class.java.classLoader) {
    val currentClassName = this.javaClass.canonicalName
    val contextClassName = Context::class.java.canonicalName

    // 1. read current class name for spi

    if (currentClassName != contextClassName && currentClassName != null) {
        readServiceClasses(currentClassName, classLoader)
    }

    if (contextClassName != null) {
        readServiceClasses(contextClassName, classLoader)
    }

}




