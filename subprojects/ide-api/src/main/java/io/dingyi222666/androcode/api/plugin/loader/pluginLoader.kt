package io.dingyi222666.androcode.api.plugin.loader

import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.plugin.PluginDescriptor

fun loadPlugin(
    ctx: Context,
    apkPath: String
): PluginDescriptor {
    val rootApkClassLoader = ApkClassLoader(
        // fake plugin id
        "system",
        apkPath,
        "$apkPath/lib"
    )

    throw Exception("Not implemented")
}