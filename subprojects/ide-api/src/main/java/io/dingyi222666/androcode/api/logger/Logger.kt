package io.dingyi222666.androcode.api.logger

class Logger(
    private val tag: String,
    private val context: LoggerContext,
) : ILogger {


    override fun info(msg: String) {
        val level = context.level
        val formatter = context.formatter
        if (level.ordinal >= LogLevel.INFO.ordinal) {
            context.readBackends.forEach {
                it.info(tag, formatter.format(tag, msg))
            }
        }
    }


    override fun info(msg: String, throwable: Throwable) {
        val level = context.level
        val formatter = context.formatter
        if (level.ordinal >= LogLevel.INFO.ordinal) {
            context.readBackends.forEach {
                it.info(tag, formatter.format(tag, msg), throwable)
            }
        }
    }

    override fun debug(msg: String) {
        val level = context.level
        val formatter = context.formatter

        if (level.ordinal >= LogLevel.DEBUG.ordinal) {
            context.readBackends.forEach {
                it.debug(tag, formatter.format(tag, msg))
            }
        }
    }

    override fun debug(msg: String, throwable: Throwable) {

        if (context.level.ordinal >= LogLevel.DEBUG.ordinal) {
            context.readBackends.forEach {
                it.debug(tag, context.formatter.format(tag, msg), throwable)
            }
        }
    }

    override fun warn(msg: String) {
        if (context.level.ordinal >= LogLevel.WARN.ordinal) {
            context.readBackends.forEach {
                it.warn(tag, context.formatter.format(tag, msg))
            }
        }
    }

    override fun warn(msg: String, throwable: Throwable) {
        if (context.level.ordinal >= LogLevel.WARN.ordinal) {
            context.readBackends.forEach {
                it.warn(tag, context.formatter.format(tag, msg), throwable)
            }
        }
    }

    override fun error(msg: String) {
        if (context.level.ordinal >= LogLevel.ERROR.ordinal) {
            context.readBackends.forEach {
                it.error(tag, context.formatter.format(tag, msg))
            }
        }
    }

    override fun error(msg: String, throwable: Throwable) {
        if (context.level.ordinal >= LogLevel.ERROR.ordinal) {
            context.readBackends.forEach {
                it.error(tag, context.formatter.format(tag, msg), throwable)
            }
        }
    }

    override fun fatal(msg: String) {
        if (context.level.ordinal >= LogLevel.FATAL.ordinal) {
            context.readBackends.forEach {
                it.fatal(tag, context.formatter.format(tag, msg))
            }
        }
    }


}