package io.dingyi222666.androcode.api.context

import androidx.annotation.CallSuper
import io.dingyi222666.androcode.api.common.Disposable
import io.dingyi222666.androcode.api.common.disposer
import io.dingyi222666.androcode.api.coroutine
import io.dingyi222666.androcode.api.event.Event
import io.dingyi222666.androcode.api.event.event


open class Context(
    open val id: String,
    val parent: Context? = null
) : Disposable {

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

    fun register(service: Service, id: String = service.id): Disposable {
        globalServices[id] = service

        disposer.register(service, this)

        return Disposable {
            removeInstance(id)
            service.dispose()
        }
    }

    fun registerConstructor(
        id: String,
        constructor: ContextConstructor
    ): Disposable {
        constructors[id] = constructor

        return Disposable {
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

    suspend fun start() {
        event.emit(
            ContextLifecycleEvent(
                ContextLifecycleEventType.STARTED,
                this@Context
            )
        )
    }

    fun fork(id: String): Context {
        return Context(id, this)
    }

    suspend internal fun disposeAsync() {
        event.emit(
            ContextLifecycleEvent(
                ContextLifecycleEventType.DISPOSED,
                this@Context

            )
        )

        disposer.disposeChild(this@Context)
        globalServices.clear()
        constructors.clear()
    }

    override fun dispose() {
        this.coroutine.launchOnMain {
            disposeAsync()
        }
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

interface Service : Disposable {
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

data class ContextLifecycleEvent(
    val type: ContextLifecycleEventType,
    val context: Context
) : Event

enum class ContextLifecycleEventType {
    STARTED,
    DISPOSED,
    /* PAUSE,
     RESTART,
     RESUME,*/
}