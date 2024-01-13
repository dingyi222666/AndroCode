package io.dingyi222666.androcode.api.logger

import io.dingyi222666.androcode.annotation.AutoGenerateServiceExtension
import io.dingyi222666.androcode.annotation.AutoService
import io.dingyi222666.androcode.api.CoroutineService
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service
import io.dingyi222666.androcode.api.ui.UIService


class LogService(
    override val ctx: Context,
    val parent: LogService? = null
) : Service {
    override val id = "log"

    private val loggerContext = LoggerContext()

    val current: Logger

    var level: LogLevel
        set(value) {
            loggerContext.level = value
        }
        get() = loggerContext.level

    var formatter: LoggerFormatter
        set(value) {
            loggerContext.formatter = value
        }
        get() = loggerContext.formatter

    fun addBackend(backend: LoggerBackend) {
        loggerContext.addBackend(backend)
    }

    fun removeBackend(backend: LoggerBackend) {
        loggerContext.removeBackend(backend)
    }

    init {
        loggerContext.tag = if (parent == null) {
            "root"
        } else {
            parent.loggerContext.tag + " > " + ctx.id
        }

        current = Logger(loggerContext.tag, loggerContext)
    }

    fun logger(tag: String): Logger {
        return Logger(loggerContext.tag + ">" + tag, loggerContext)
    }

    override fun fork(parent: Context?): LogService {
        if (parent == ctx || parent == null) {
            return this
        }

        return LogService(parent, this)
    }

    override fun dispose() {
        super.dispose()
        loggerContext.clear()
    }
}

@AutoService(Context::class, "log")
@AutoGenerateServiceExtension(Context::class, "log", "log")
fun createLoggerService(ctx: Context): LogService {
    val parent = ctx.parent?.getOrNull<LogService>("log", false)

    return parent?.fork(ctx) ?: LogService(ctx)
}