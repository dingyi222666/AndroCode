package io.dingyi222666.androcode.api.common

import com.google.common.collect.HashMultimap
import io.dingyi222666.androcode.annotation.AutoGenerateServiceExtension
import io.dingyi222666.androcode.annotation.AutoService
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service

fun interface Disposable {
    fun dispose()
}


class Disposer(
    override val ctx: Context
) : Service, Disposable {

    override val id = "disposer"

    private val objectTree = HashMultimap.create<Disposable, Disposable>()

    fun register(child: Disposable, parent: Disposable) {
        objectTree.put(parent, child)
    }

    override fun dispose() {
        super.dispose()

        objectTree.keySet().forEach {
            disposeChild(it)
        }
    }

    fun disposeChild(parent: Disposable) {
        val disposableList = mutableListOf<Disposable>()
        val disposableStack = ArrayDeque<Disposable>()

        disposableStack.add(parent)

        while (disposableStack.isNotEmpty()) {
            val disposable = disposableStack.removeLast()

            if (disposable != parent && disposable != this && !disposableList.contains(disposable)) {
                disposableList.add(disposable)
            }

            if (!objectTree.containsKey(disposable)) {
                continue
            }
            disposableStack.addAll(
                objectTree[disposable]
            )
        }

        for (disposable in disposableList) {
            disposable.dispose()
        }
    }

    fun markAsDisposed(disposable: Disposable) {
        // search disposable

        // keys

        if (objectTree.containsKey(disposable)) {
            disposeChild(disposable)

            return
        }

        if (objectTree.containsValue(disposable)) {
            // search ...

            objectTree.entries().filter {
                it.value == disposable
            }.toList().forEach {
                objectTree.remove(it.key, it.value)
            }
        }

    }
}

class DisposableStore : Disposable {
    private val disposables = ArrayList<Disposable>()
    fun add(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun dispose() {

        for (disposable in disposables) {
            disposable.dispose()
        }
    }

    fun clear() {
        disposables.clear()
    }

    fun size() = disposables.size

    fun remove(disposable: Disposable) {
        disposables.remove(disposable)
    }
}

@AutoService(Context::class, "disposer")
@AutoGenerateServiceExtension(Context::class, "disposer", "disposer")
fun createDisposer(ctx: Context): Disposer {
    return Disposer(ctx)
}
