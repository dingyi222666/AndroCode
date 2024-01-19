package io.dingyi222666.androcode.api.plugin.loader

import android.app.Application
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.plugin.AndroCodePlugin
import io.dingyi222666.androcode.api.plugin.PluginDescriptor
import io.dingyi222666.androcode.api.storage.storage
import java.io.File

fun loadPlugins(
    ctx: Context,
    androidApplication: Application,
    parentClassLoader: CombineClassLoader,
    apkPath: String
): List<PluginDescriptor> {

    val packageManager = androidApplication.packageManager

    val apkPackageInfo = packageManager.getPackageArchiveInfo(apkPath, 0)
        ?: throw Exception("can not get apk package info")

    val versionCode = apkPackageInfo.versionCode

    val packageName = apkPackageInfo.packageName

    val libDirectory = File("$apkPath/lib")

    val lastVersionCode = ctx.storage.coreRootStorage.getInt("$packageName-versionCode", -1)

    if (!libDirectory.isDirectory || lastVersionCode == -1 || lastVersionCode < versionCode) {
          // TODO: unzip apk
    }


    val rootApkClassLoader = ApkClassLoader(
        // fake plugin id
        "system",
        apkPath,
        "$apkPath/lib"
    )


    // first, find all plugin main classes

    val pluginMainClasses =
        rootApkClassLoader
            .getResourceAsStream("META-INF/services/io.dingyi222666.androcode.api.plugin.PluginMain")
            .bufferedReader()
            .use { bufferedReader -> bufferedReader.readLines().map { it.trim() } }


    return pluginMainClasses.map { pluginMainClassName ->

        val spec = FilteringClassLoader.Spec()


        spec.apply {
            allowPackage(pluginMainClassName.substringBeforeLast("."))

            allowClass(pluginMainClassName)
        }

        parentClassLoader.addClassLoader(rootApkClassLoader)

        val filteringClassLoader = FilteringClassLoader(
            parentClassLoader,
            "system",
            spec,
        )

        val pluginMainClass: Class<AndroCodePlugin> =
            filteringClassLoader.loadClass(pluginMainClassName) as Class<AndroCodePlugin>


        val instance = pluginMainClass.getConstructor().newInstance()

        val pluginConfig = instance.config()

        rootApkClassLoader.pluginId = pluginConfig.id

        // 放宽到允许所有
        filteringClassLoader.spec.allowAllPackage()
        filteringClassLoader.refreshSpec()

        PluginDescriptor(
            rawPlugin = instance,
            pluginId = pluginConfig.id,
            pluginConfig = pluginConfig,
            pluginDexClassLoader = rootApkClassLoader,
            pluginFilteringClassLoader = filteringClassLoader,
            pluginApkPath = apkPath
        )
    }

}

fun loadPlugin(
    ctx: Context,
    androidApplication: Application,
    pluginId: String,
    parentClassLoader: CombineClassLoader,
    apkPath: String
): PluginDescriptor {
    return loadPlugins(ctx, androidApplication, parentClassLoader, apkPath).find {
        it.pluginId == pluginId
    } ?: throw IllegalArgumentException("Plugin not found: $pluginId")
}

fun loadSystemPlugins(): List<PluginDescriptor> {

    val rootClassLoader = PluginDescriptor::class.java.classLoader
        ?: throw IllegalStateException("rootClassLoader is null")

    val pluginMainClasses =
        rootClassLoader
            .getResourceAsStream("META-INF/services/io.dingyi222666.androcode.api.plugin.PluginMain")
            .bufferedReader()
            .use { bufferedReader -> bufferedReader.readLines().map { it.trim() } } ?: emptyList()

    return pluginMainClasses.map { pluginMainClassName ->


        val pluginMainClass: Class<AndroCodePlugin> =
            rootClassLoader.loadClass(pluginMainClassName) as Class<AndroCodePlugin>


        val instance = pluginMainClass.getConstructor().newInstance()

        val pluginConfig = instance.config()

        PluginDescriptor(
            rawPlugin = instance,
            pluginId = pluginConfig.id,
            pluginConfig = pluginConfig,
            pluginDexClassLoader = null,
            pluginFilteringClassLoader = null,
            pluginApkPath = null
        )
    }
}