package io.dingyi222666.rewrite.androlua.api.service

import io.dingyi222666.rewrite.androlua.api.common.IDisposable

internal class ServiceRegistry(
    override val name: String,
    override val parent: IServiceRegistry? = null
) : IServiceRegistry() {

    private val globalServices = mutableMapOf<String, Service>()

    private val constructors = mutableMapOf<String, ((IServiceRegistry) -> Service)>()

    override fun removeInstance(id: String) {
        if (globalServices.contains(id)) {
            globalServices.remove(id)
        }
    }

    override fun removeConstructor(id: String) {
        if (constructors.contains(id)) {
            constructors.remove(id)
        }
    }

    override fun register(service: Service): IDisposable {
        globalServices[service.name] = service

        return IDisposable {
            removeInstance(service.name)
            service.dispose()
        }
    }

    override fun registerConstructor(
        id: String,
        constructor: (IServiceRegistry) -> Service
    ): IDisposable {
        constructors[id] = constructor

        return IDisposable {
            removeConstructor(id)
            removeInstance(id)
        }
    }

    override fun <T : Service> get(id: String): T {
        var rawService =
            globalServices[id]

        if (rawService != null) {
            return rawService as T
        }

        val constructor = constructors[id]

        if (constructor != null) {
            rawService = constructor(this)
        }

        if (rawService != null) {
            return rawService as T
        }

        if (parent != null) {
            return parent.get(id) as T
        }

        throw IllegalStateException("Service $id not found")
    }

    override fun dispose(id: String) {
        val service = globalServices[id]

        if (service != null) {
            service.dispose()
        } else {
            // TODO: error
        }
    }
}


inline fun <reified T : Service> IServiceRegistry.getAs(id: String): T {
    val service = get<Service>(id)

    if (service is T) {
        return service
    }

    throw IllegalStateException("Service $id is not of type ${T::class.java}")
}

interface Service : IDisposable {
    val name: String

    val registry: IServiceRegistry
    override fun dispose() {
        registry.removeInstance(name)
    }
}