package io.dingyi222666.androcode.api.event

import com.google.common.collect.HashMultimap
import io.dingyi222666.androcode.api.Androcode
import io.dingyi222666.androcode.api.common.IDisposable
import io.dingyi222666.androcode.api.coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

interface Event

fun interface EventListener<E : Event> {
    suspend fun onEvent(event: E)
}


open class EventEmitter(
    private val parent: EventEmitter? = null
) : IDisposable {

    private val root: EventEmitter = parent?.root ?: this

    private val listeners: HashMultimap<KClass<out Event>, EventListener<out Event>> =
        HashMultimap.create()

    private val children =
        mutableListOf<EventEmitter>()

    private val lock = Mutex(false)

    init {
        parent?.children?.add(this)
    }


    suspend fun <E : Event> emit(event: E, direction: EventDirection = EventDirection.NONE) {
        when (direction) {
            EventDirection.TO_CHILDREN -> emitChildren(event)
            EventDirection.NONE -> emitDefault(event)
            EventDirection.TO_PARENT -> emitToParent(event)
        }
    }


    private suspend fun <E : Event> emitToParent(event: E) {
        parent?.emit(event, EventDirection.TO_PARENT)
    }

    private suspend fun <E : Event> emitChildren(event: E) = withContext(Dispatchers.Main) {
        children.map {
            async {
                it.emit(event)
            }
        }.awaitAll()
    }

    private suspend fun <E : Event> emitDefault(event: E) = withContext(Dispatchers.IO) {
        val deferredList = lock.withLock {
            listeners[event::class].filterIsInstance<EventListener<E>>()
                .map {
                    async(Dispatchers.IO) {
                        it.onEvent(event)
                    }

                }
        }

        deferredList.awaitAll()
    }


    suspend fun <E : Event> off(clazz: KClass<E>, listener: EventListener<E>) {
        lock.withLock {
            listeners.remove(clazz, listener)
        }
    }

    suspend fun <E : Event> on(clazz: KClass<E>, listener: EventListener<E>): IDisposable {
        lock.withLock {
            listeners.put(clazz, listener)
        }

        return IDisposable {
            Androcode.coroutine.launchOnMain {
                off(clazz, listener)
            }
        }
    }


    override fun dispose() {
        children.clear()
    }
}

enum class EventDirection {
    TO_CHILDREN,
    TO_PARENT,
    NONE
}

suspend inline fun <reified E : Event> EventEmitter.on(
    listener: EventListener<E>,
): IDisposable {
    return on(E::class, listener)
}

suspend inline fun <reified E : Event> EventEmitter.off(listener: EventListener<E>) {
    return off(E::class, listener)
}

