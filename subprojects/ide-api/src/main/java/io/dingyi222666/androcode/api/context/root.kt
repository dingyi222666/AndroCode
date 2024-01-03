package io.dingyi222666.androcode.api.context

import androidx.annotation.CallSuper
import io.dingyi222666.androcode.api.common.IDisposable
import io.dingyi222666.androcode.api.disposer

open class Context(
    open val id: String,
    val parent: Context? = null
) : IDisposable {

    private val globalServices: MutableMap<String, Service> = mutableMapOf()

    private val constructors: MutableMap<String, ContextConstructor> = mutableMapOf()

    val root: Context by lazy(LazyThreadSafetyMode.NONE) {
        if (parent == null) {
            return@lazy this
        }

        var current: Context = this

        while (true) {
            val parent = current.parent ?: break

            current = parent
        }

        return@lazy current
    }

    fun removeInstance(id: String) {
        if (globalServices.contains(id)) {
            globalServices.remove(id)
        }
    }

    fun removeConstructor(id: String) {
        if (constructors.contains(id)) {
            constructors.remove(id)
        }
    }

    fun register(service: Service, id: String = service.id): IDisposable {
        globalServices[id] = service

        disposer.register(service, this)

        return IDisposable {
            removeInstance(id)
            service.dispose()
        }
    }

    fun registerConstructor(
        id: String,
        constructor: ContextConstructor
    ): IDisposable {
        constructors[id] = constructor

        return IDisposable {
            removeConstructor(id)
            removeInstance(id)
        }
    }

    fun <T : Service> get(id: String): T {
        val service =
            getOrNull<T>(id)
        return service ?: throw IllegalStateException("Service $id not found")
    }

    fun <T : Service> getOrNull(id: String, create: Boolean = true): T? {
        var rawService =
            globalServices[id]

        if (rawService != null) {
            @Suppress("UNCHECKED_CAST")
            return rawService as T
        }

        val constructor = constructors[id]

        if (constructor != null && create) {
            rawService = constructor(this)
        }

        if (rawService != null) {
            // globalServices[id] = rawService
            register(rawService, id)
            return rawService as T
        }

        val parent = parent

        val result = parent?.getOrNull<T>(id)

        if (result != null) {
            return result
        }


        return null
    }

    fun dispose(id: String) {
        val service = globalServices[id]

        if (service != null) {
            service.dispose()
        } else {
            // TODO: error
        }
    }

    fun fork(id: String): Context {
        return Context(id, this)
    }

    override fun dispose() {
        disposer.disposeChild(this)
        globalServices.clear()
        constructors.clear()
    }
}


typealias ContextConstructor = (Context) -> Service

inline fun <reified T : Service> Context.getAs(id: String): T {
    val service = get<Service>(id)

    if (service is T) {
        return service
    }

    throw IllegalStateException("Service $id is not of type ${T::class.java}")
}

interface Service : IDisposable {
    val id: String

    val ctx: Context

    @CallSuper
    override fun dispose() {
        ctx.removeInstance(id)
    }

    fun fork(parent: Context?): Service {
        // override this method if you need to fork
        return this
    }
}