package io.dingyi222666.rewrite.androlua.api.command

interface CommandHandler<T : Any> {
    fun execute(vararg input: Any): T
}

interface ICommandRegistry {}
