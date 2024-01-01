package io.dingyi222666.rewrite.androlua.logger

import android.util.Log

interface ILogger {
    fun info(msg: String)

    fun info(msg: String, throwable: Throwable)

    fun debug(msg: String)

    fun debug(msg: String, throwable: Throwable)

    fun warn(msg: String)

    fun warn(msg: String, throwable: Throwable)

    fun error(msg: String)

    fun error(msg: String, throwable: Throwable)

    fun fatal(msg: String)
}

interface LoggerBackend {

    object AndroidDefault : LoggerBackend {
        override fun info(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        override fun info(tag: String, msg: String, throwable: Throwable) {
            Log.i(tag, msg, throwable)
        }

        override fun debug(tag: String, msg: String) {
            Log.d(tag, msg)
        }

        override fun debug(tag: String, msg: String, throwable: Throwable) {
            Log.d(tag, msg, throwable)
        }

        override fun warn(tag: String, msg: String) {
            Log.w(tag, msg)
        }

        override fun warn(tag: String, msg: String, throwable: Throwable) {
            Log.w(tag, msg, throwable)
        }

        override fun error(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        override fun error(tag: String, msg: String, throwable: Throwable) {
            Log.e(tag, msg, throwable)
        }

        override fun fatal(tag: String, msg: String) {
            Log.wtf(tag, msg)
        }


    }

    fun info(tag: String, msg: String)

    fun info(tag: String, msg: String, throwable: Throwable)

    fun debug(tag: String, msg: String)

    fun debug(tag: String, msg: String, throwable: Throwable)

    fun warn(tag: String, msg: String)

    fun warn(tag: String, msg: String, throwable: Throwable)

    fun error(tag: String, msg: String)

    fun error(tag: String, msg: String, throwable: Throwable)

    fun fatal(tag: String, msg: String)
}

interface LoggerFormatter {

    object Default : LoggerFormatter {
        override fun format(tag: String, msg: String, throwable: Throwable?): String {
            return if (throwable != null) {
                "[$tag] $msg ${throwable.message}"
            } else {
                "[$tag] $msg"
            }
        }
    }

    fun format(tag: String, msg: String, throwable: Throwable? = null): String
}

class LoggerContext {
    private val backends = mutableListOf<LoggerBackend>(
        LoggerBackend.AndroidDefault
    )

    val readBackends: List<LoggerBackend> = backends

    var level: LogLevel = LogLevel.INFO

    var tag = ""

    fun addBackend(backend: LoggerBackend) {
        backends += backend
    }

    var formatter: LoggerFormatter = LoggerFormatter.Default

    fun removeBackend(backend: LoggerBackend) {
        backends -= backend
    }

    fun clear() {
        backends.clear()
    }
}

enum class LogLevel {
    NONE,
    INFO,
    DEBUG,
    WARN,
    ERROR,
    FATAL
}