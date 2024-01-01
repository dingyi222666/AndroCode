package io.dingyi222666.androcode.api.plugin.loader

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

    private val classLoaders = classLoaders.toMutableList()

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

}