package io.dingyi222666.rewrite.androlua.api.common

import com.google.common.collect.HashMultimap
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service

fun interface IDisposable {
    fun dispose()
}

class Disposer(
    override val ctx: Context
) : Service, IDisposable {

    override val id = "disposer"

    private val objectTree = HashMultimap.create<IDisposable, IDisposable>()

    fun register(child: IDisposable, parent: IDisposable) {
        objectTree.put(parent, child)
    }

    override fun dispose() {
        super.dispose()

        objectTree.keySet().forEach {
            disposeChild(it)
        }
    }

    fun disposeChild(parent: IDisposable) {
        val disposableList = mutableListOf<IDisposable>()
        val disposableStack = ArrayDeque<IDisposable>()

        disposableStack.add(parent)

        while (disposableStack.isNotEmpty()) {
            val disposable = disposableStack.removeLast()

            if (disposable != parent) {
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

    fun markAsDisposed(disposable: IDisposable) {
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

class DisposableStore : IDisposable {
    private val disposables = ArrayList<IDisposable>()
    fun add(disposable: IDisposable) {
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

    fun remove(disposable: IDisposable) {
        disposables.remove(disposable)
    }
}

fun createDisposer(ctx: Context): Disposer {
    return Disposer(ctx)
}
