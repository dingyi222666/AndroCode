package io.dingyi222666.androcode.api.plugin.loader

import java.io.IOException
import java.net.URL
import java.util.Enumeration

/**
 *
 *      FilterClassLoader?  ->   CombineClassLoader    -> AppClassLoader
 *                               (child)
 *                               PluginADexClassLoader
 *                               PluginBDexClassLoader
 *
 */
class CombineClassLoader(classLoaders: Array<out ClassLoader>, parent: ClassLoader) :
    ClassLoader(parent) {

    internal val classLoaders = classLoaders.toMutableList()

    internal fun addClassLoader(classLoader: ClassLoader) {
        classLoaders.add(classLoader)
    }

    override fun findClass(name: String): Class<*> {
        var c: Class<*>? = findLoadedClass(name)
        val classNotFoundException = ClassNotFoundException(name)
        if (c == null) {
            try {
                c = super.findClass(name)
            } catch (e: ClassNotFoundException) {
                classNotFoundException.addSuppressed(e)
            }

            if (c == null) {
                for (classLoader in classLoaders) {
                    try {
                        c = classLoader.loadClass(name)!!
                        break
                    } catch (e: ClassNotFoundException) {
                        classNotFoundException.addSuppressed(e)
                    }
                }
                if (c == null) {
                    throw classNotFoundException
                }
            }
        }
        return c
    }


    override fun getResource(name: String): URL? {
        var url: URL? = null

        for (classLoader in classLoaders) {
            url = classLoader.getResource(name)
            if (url != null) {
                break
            }
        }

        if (url == null) {
            url = super.getResource(name)
        }

        return url
    }

    @Throws(IOException::class)
    override fun getResources(name: String): Enumeration<URL>? {
        var urls: Enumeration<URL>? = null

        for (classLoader in classLoaders) {
            urls = classLoader.getResources(name)
            if (urls != null) {
                break
            }
        }

        if (urls == null) {
            urls = super.getResources(name)
        }

        return urls
    }


}