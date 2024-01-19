package io.dingyi222666.androcode.api.storage

import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service
import io.dingyi222666.androcode.api.init.init

class StorageService(override val ctx: Context) : Service {
    override val id = "storage"

    internal val coreRootStorage: Storage by lazy(LazyThreadSafetyMode.NONE) {
        JSONStorage(ctx.init.androidApplication.filesDir.resolve("core.json"))
    }

    private val storageMap = mutableMapOf<String, Storage>()

    val rootStorage by lazy(LazyThreadSafetyMode.NONE) {
        JSONStorage(ctx.init.storageDirectory.resolve("root.json"))
    }

    val currentWorkspaceStorage by lazy(LazyThreadSafetyMode.NONE) {
        // TODO: get current workspace
    }

    fun createStorage(name: String): Storage {
        TODO("??? ")
    }


    override fun dispose() {
        super.dispose()
        for (storage in storageMap.values) {
            storage.dispose()
        }
        storageMap.clear()

    }


}

