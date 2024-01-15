package io.dingyi222666.androcode.plugin.testplugin

import io.dingyi222666.androcode.annotation.AutoGenerateServiceExtension
import io.dingyi222666.androcode.annotation.PluginMain
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service
import io.dingyi222666.androcode.api.logger.log

import io.dingyi222666.androcode.api.plugin.AndroCodePlugin
import io.dingyi222666.androcode.api.plugin.buildPluginConfig


@PluginMain
class PluginTest1 : AndroCodePlugin {
    override suspend fun activate(ctx: Context) {
        ctx.log.current.info("load plugin test1")
        ctx.root.registerConstructor("serviceTest1", ::createServiceTest1)
    }

    override fun config() = buildPluginConfig {
        packageName = "com.plugin.test1"
        displayName = "test1"
        version = "1.0.0"
        sdkVersion = 1
        activationEvents {
            event("onLanguage:kotlin")
        }
    }
}

class ServiceTest1(override val ctx: Context) : Service {
    override val id = "serviceTest1"

    init {
        ctx.log.current.info("load service test1")
    }

    fun format(s: String, n: Int) = s.repeat(n)
}


@AutoGenerateServiceExtension(Context::class, "serviceTest1", "serviceTest1")
fun createServiceTest1(ctx: Context): ServiceTest1 {
    val root = ctx.root.getOrNull<ServiceTest1>("serviceTest1", false)

    return root ?: ServiceTest1(ctx)
}

@PluginMain
class PluginTest2 : AndroCodePlugin {
    override suspend fun activate(ctx: Context) {
        ctx.log.current.info("load plugin test2")
        ctx.log.current.warn(ctx.serviceTest1.format("test servive test1", 10))
    }

    override fun config() = buildPluginConfig {
        packageName = "com.plugin.test2"
        displayName = "test2"
        version = "1.0.0"
        sdkVersion = 1
        activationEvents {
            event("onLanguage:kotlin")
        }
        dependencies {
            // plugin("com.plugin.test1", "1.0.+")
            plugin("com.plugin.test1:2.0")
        }
    }
}