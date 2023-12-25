package io.dingyi222666.rewrite.androlua.api.service


import io.dingyi222666.rewrite.androlua.api.common.IDisposable

abstract class IServiceRegistry {

    abstract val name: String

    abstract val parent: IServiceRegistry?

    abstract fun register(service: Service): IDisposable

    abstract fun registerConstructor(
        id: String,
        constructor: (IServiceRegistry) -> Service
    ): IDisposable

    abstract fun <T : Service> get(id: String): T

    abstract fun dispose(id: String)

    internal abstract fun removeInstance(id: String)

    internal abstract fun removeConstructor(id: String)
}