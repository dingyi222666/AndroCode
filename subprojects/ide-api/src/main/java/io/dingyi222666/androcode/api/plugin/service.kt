package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.configureBase
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.ContextLifecycleEvent
import io.dingyi222666.androcode.api.context.ContextLifecycleEventType
import io.dingyi222666.androcode.api.context.Service
import io.dingyi222666.androcode.api.coroutine
import io.dingyi222666.androcode.api.event.event
import io.dingyi222666.androcode.api.event.on
import io.dingyi222666.androcode.api.plugin.loader.ApkClassLoader
import io.dingyi222666.androcode.api.plugin.loader.CombineClassLoader
import io.dingyi222666.androcode.api.plugin.loader.FilteringClassLoader
import java.lang.ClassLoader.getSystemClassLoader

class PluginService(
    override val ctx: Context
) : Service {


    private val container = PluginContainer(ctx)

    init {
        ctx.coroutine.launchOnMain {
            ctx.event.on<ContextLifecycleEvent>(::onActivate)
        }
    }

    fun onActivate(event: ContextLifecycleEvent) {
        if (event.type != ContextLifecycleEventType.STARTED) {
            return
        }


    }

    override fun dispose() {
        super.dispose()
    }

    override val id = "plugin"

}

class PluginContainer(
    private val ctx: Context
) {

    private val combineClassLoader = CombineClassLoader(emptyArray(), getSystemClassLoader())

    private val plugins = mutableMapOf<String, PluginDescriptor>()

    private val activePlugins = mutableMapOf<String, ActivatedPluginDescriptor>()


    fun loadPlugins(apkPath: String) {
        val pluginDescriptors =
            io.dingyi222666.androcode.api.plugin.loader.loadPlugins(combineClassLoader, apkPath)

        for (pluginDescriptor in pluginDescriptors) {
            plugins[pluginDescriptor.pluginId] = pluginDescriptor
        }
    }

    fun loadSystemPlugins() {
        val pluginDescriptors =
            io.dingyi222666.androcode.api.plugin.loader.loadSystemPlugins()

        for (pluginDescriptor in pluginDescriptors) {
            plugins[pluginDescriptor.pluginId] = pluginDescriptor
        }
    }

    fun activatePlugin(pluginId: String) {
        if (activePlugins.containsKey(pluginId)) {
            throw IllegalArgumentException("Plugin already activated: $pluginId")
        }

        if (!plugins.containsKey(pluginId)) {
            throw IllegalArgumentException("Plugin not found: $pluginId")
        }

        val pluginDescriptor = plugins.getValue(pluginId)

        // fork context

        val fork = ctx.fork(pluginDescriptor.pluginId)

        fork.configureBase(
            pluginDescriptor.pluginFilteringClassLoader ?: this::class.java.classLoader
        )

        activePlugins[pluginId] = ActivatedPluginDescriptor(pluginDescriptor, fork)
    }

    fun disposePlugin(pluginId: String) {
        if (!activePlugins.containsKey(pluginId)) {
            throw IllegalArgumentException("Plugin not activated: $pluginId")
        }

        val activatedPluginDescriptor = activePlugins.getValue(pluginId)

        ctx.coroutine.launchOnMain {
            activatedPluginDescriptor.pluginContext.disposeAsync()

            activePlugins.remove(pluginId)

            if (pluginId.startsWith("system.")) {
                return@launchOnMain
            }

            combineClassLoader.removeClassLoader {
                it is ApkClassLoader && it.pluginId == pluginId
            }
        }
    }

    fun reloadPlugin(pluginId: String) {
        val activatedPluginDescriptor = activePlugins.getValue(pluginId)

        disposePlugin(pluginId)

        if (!pluginId.startsWith("system.")) {
            // system plugin always not reloadable
            plugins[pluginId] = io.dingyi222666.androcode.api.plugin.loader.loadPlugin(
                pluginId,
                combineClassLoader,
                activatedPluginDescriptor.pluginDescriptor.pluginApkPath
                    ?: throw IllegalArgumentException("Plugin not found: $pluginId")
            )
        }

        activatePlugin(pluginId)
    }

    suspend fun dispose() {
        for (activatedPluginDescriptor in activePlugins.values) {
            activatedPluginDescriptor.pluginContext.disposeAsync()

            activePlugins.remove(activatedPluginDescriptor.pluginDescriptor.pluginId)
        }

        plugins.clear()
    }
}

data class PluginDescriptor(
    val rawPlugin: AndroCodePlugin,
    val pluginId: String,
    val pluginConfig: PluginConfig,
    val pluginApkPath: String?,
    val pluginFilteringClassLoader: FilteringClassLoader?,
    val pluginDexClassLoader: ApkClassLoader?
)


data class ActivatedPluginDescriptor(
    val pluginDescriptor: PluginDescriptor,
    val pluginContext: Context
)