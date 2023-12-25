package io.dingyi222666.rewrite.androlua.api.common
fun interface IDisposable {
    fun dispose()
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