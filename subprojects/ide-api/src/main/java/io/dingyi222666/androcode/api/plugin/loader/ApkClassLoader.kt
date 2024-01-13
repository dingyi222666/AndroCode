package io.dingyi222666.androcode.api.plugin.loader

import dalvik.system.DexClassLoader

class ApkClassLoader(
    internal var pluginId: String,
    private val apkPath: String,
    private val libraryPath: String,
    private val parentClassLoader: ClassLoader = getSystemClassLoader()
) : DexClassLoader(apkPath, "$apkPath/optimized_dex", libraryPath, parentClassLoader) {

}