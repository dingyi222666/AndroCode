package io.dingyi222666.androcode.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class AutoService(
    val targetClass: KClass<*>,
    val id: String
)

@Target(AnnotationTarget.FUNCTION)
annotation class AutoGenerateServiceExtension(
    val targetClass: KClass<*>,
    val id: String,
    val valName: String,
)

@Target(AnnotationTarget.CLASS)
annotation class PluginMain