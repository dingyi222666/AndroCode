package io.dingyi222666.rewrite.androlua.api.command

import io.dingyi222666.rewrite.androlua.api.common.IDisposable
import io.dingyi222666.rewrite.androlua.api.service.IServiceRegistry
import io.dingyi222666.rewrite.androlua.api.service.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun interface ICommandHandler<T : Any> {
    fun execute(vararg input: Any?): T
}

data class ICommand(
    val id: String, val handler: ICommandHandler<*>
)

interface ICommandRegistry {
    fun registerCommand(id: String, command: ICommandHandler<*>): IDisposable
    fun registerCommand(command: ICommand): IDisposable
    fun registerCommandAlias(oldId: String, newId: String): IDisposable
    fun getCommand(id: String): ICommand?
    fun getCommands(): Map<String, ICommand>
}

internal class CommandRegistry : ICommandRegistry {

    private val commands = mutableMapOf<String, ICommand>()

    override fun registerCommand(id: String, command: ICommandHandler<*>): IDisposable {
        return registerCommand(ICommand(id, command))
    }

    override fun registerCommand(command: ICommand): IDisposable {

        val (id) = command

        if (commands.containsKey(id)) {
            error("Command with id $id already exists")
        }

        commands[id] = command

        return IDisposable {
            commands.remove(id)
        }
    }

    override fun registerCommandAlias(oldId: String, newId: String): IDisposable {
        val command = commands[oldId] ?: error("Command with id $oldId does not exist")
        return registerCommand(newId) {
            command.handler.execute(*it)
        }
    }

    override fun getCommand(id: String): ICommand? {
        return commands[id]
    }

    override fun getCommands(): Map<String, ICommand> {
        return commands.toMap()
    }

}

abstract class ICommandService {
    internal abstract val commandRegistry: ICommandRegistry

    fun registerCommand(id: String, command: ICommandHandler<*>): IDisposable =
        commandRegistry.registerCommand(id, command)

    fun registerCommandAlias(oldId: String, newId: String): IDisposable =
        commandRegistry.registerCommandAlias(oldId, newId)

    fun getCommand(id: String): ICommand? = commandRegistry.getCommand(id)

    fun getCommands(): Map<String, ICommand> = commandRegistry.getCommands()

    fun <T : Any> executeCommand(id: String, vararg args: Any?): T {
        val command = getCommand(id) ?: error("Command $id not found")

        return command.handler.execute(*args) as T
    }

    suspend fun <T : Any> executeCommandAsync(id: String, vararg args: Any?): T {
        val command = getCommand(id) ?: error("Command $id not found")

        val result = withContext(Dispatchers.IO) {
            command.handler.execute(*args) as T
        }

        return result
    }
}

class CommandService internal constructor(
    serviceRegistry: IServiceRegistry, registry: ICommandRegistry?
) : ICommandService(), Service {
    override val name = "command"
    override val commandRegistry: ICommandRegistry = registry ?: CommandRegistry()
    override val registry: IServiceRegistry = serviceRegistry
}

fun createCommandService(
    serviceRegistry: IServiceRegistry, commandRegistry: ICommandRegistry? = null
): CommandService {
    return CommandService(serviceRegistry, commandRegistry)
}

