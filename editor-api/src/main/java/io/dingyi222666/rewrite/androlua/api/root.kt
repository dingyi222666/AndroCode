package io.dingyi222666.rewrite.androlua.api

import io.dingyi222666.rewrite.androlua.api.command.CommandService
import io.dingyi222666.rewrite.androlua.api.command.ICommandService
import io.dingyi222666.rewrite.androlua.api.command.createCommandService
import io.dingyi222666.rewrite.androlua.api.common.IDisposable
import io.dingyi222666.rewrite.androlua.api.service.IServiceRegistry
import io.dingyi222666.rewrite.androlua.api.service.Service
import io.dingyi222666.rewrite.androlua.api.service.ServiceRegistry
import io.dingyi222666.rewrite.androlua.api.service.getAs

object AndroLua : IServiceRegistry() {

    private val rootRegistry = ServiceRegistry("root")

    init {
        registerConstructor("coroutine", ::createCoroutineService)
        registerConstructor("command", ::createCommandService)
    }

    val coroutine: CoroutineService by lazy(LazyThreadSafetyMode.NONE) {
        getAs<CoroutineService>("coroutine")
    }

    val command: ICommandService by lazy(LazyThreadSafetyMode.NONE) {
        getAs<CommandService>("command")
    }

    override val name: String = "root"
    override val parent: IServiceRegistry? = null

    override fun register(service: Service) =
        rootRegistry.register(service)

    override fun registerConstructor(
        id: String,
        constructor: (IServiceRegistry) -> Service
    ) =
        rootRegistry.registerConstructor(id, constructor)

    override fun <T : Service> get(id: String) =
        rootRegistry.get<T>(id)

    override fun dispose(id: String) =
        rootRegistry.dispose(id)

    override fun removeInstance(id: String) =
        rootRegistry.removeInstance(id)

    override fun removeConstructor(id: String) =
        rootRegistry.removeConstructor(id)

}




