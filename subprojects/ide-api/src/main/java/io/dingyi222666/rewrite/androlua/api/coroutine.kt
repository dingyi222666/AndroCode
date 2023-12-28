@file:JvmName("xxx")

package io.dingyi222666.rewrite.androlua.api

import io.dingyi222666.rewrite.androlua.annotation.AutoService
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


class CoroutineService internal constructor(
    override val ctx: Context
) : Service {


    override val id = "coroutine"

    @get:JvmName("xxxx")
    val rootJob = SupervisorJob()
    val rootCoroutine = CoroutineScope(Dispatchers.IO + rootJob)


    fun launchOnIO(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) =
        rootCoroutine.launch(Dispatchers.IO, start, block)

    fun launchOnMain(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = rootCoroutine.launch(Dispatchers.Main, start, block)

    override fun dispose() {
        super.dispose()

        rootJob.cancel()
        rootJob.cancelChildren()
    }
}

@AutoService(AndroLua::class)
fun createCoroutineService(registry: Context): CoroutineService {
    return CoroutineService(registry)
}