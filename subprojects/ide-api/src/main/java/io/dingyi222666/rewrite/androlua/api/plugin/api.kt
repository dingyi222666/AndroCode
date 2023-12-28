package io.dingyi222666.rewrite.androlua.api.plugin

interface ReWriteAndroLuaPlugin {

    fun activate()

    fun config(): PluginConfig

}


@Target(AnnotationTarget.CLASS)
annotation class PluginMain