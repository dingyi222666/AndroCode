package io.dingyi222666.rewrite.androlua.api.command

import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.common.IDisposable
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import io.dingyi222666.rewrite.androlua.api.disposer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun interface ICommandHandler<T : Any> {
    suspend fun execute(vararg input: Any?): T
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

internal class CommandRegistry(
    private val ctx: Context = AndroLua
) : ICommandRegistry {

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

        val disposable = IDisposable {
            commands.remove(id)
        }

        ctx.disposer.register(disposable, ctx)

        return IDisposable {
            disposable.dispose()
            ctx.disposer.markAsDisposed(disposable)
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

data class CommandDescriptor(
    val id: String,
    val title: String,
    val description: String
)


abstract class ICommandService : Service {
    internal abstract val commandRegistry: ICommandRegistry

    internal abstract val descriptionList: MutableList<CommandDescriptor>

    fun registerCommand(id: String, command: ICommandHandler<*>): IDisposable =
        commandRegistry.registerCommand(id, command)

    fun registerCommandAlias(oldId: String, newId: String): IDisposable =
        commandRegistry.registerCommandAlias(oldId, newId)

    fun registerCommandDescription(id: String, title: String, description: String): IDisposable {
        descriptionList.add(CommandDescriptor(id, title, description))

        val disposable = IDisposable {
            descriptionList.remove(CommandDescriptor(id, title, description))
        }

        ctx.disposer.register(disposable, ctx)

        return IDisposable {
            disposable.dispose()
            ctx.disposer.markAsDisposed(disposable)
        }
    }

    fun getCommand(id: String): ICommand? = commandRegistry.getCommand(id)

    fun getCommandDescription(id: String): CommandDescriptor {
        return descriptionList.find { it.id == id } ?: CommandDescriptor(
            id, title = id, description = ""
        )
    }

    fun getCommands(): Map<String, ICommand> = commandRegistry.getCommands()

    suspend fun <T : Any> executeCommand(id: String, vararg args: Any?): T {
        val command = getCommand(id) ?: error("Command $id not found")

        val result = withContext(Dispatchers.IO) {
            @Suppress("UNCHECKED_CAST")
            command.handler.execute(*args) as T
        }

        return result
    }
}

class CommandService internal constructor(
    override val ctx: Context, registry: ICommandRegistry?
) : ICommandService() {
    override val id = "command"
    override val commandRegistry: ICommandRegistry = registry ?: CommandRegistry(this.ctx)
    override val descriptionList = mutableListOf<CommandDescriptor>()

    override fun fork(parent: Context?): CommandService {
        return CommandService(parent ?: ctx, commandRegistry)
    }
}

fun createCommandService(
    ctx: Context, commandRegistry: ICommandRegistry? = null
): CommandService {
    val root = ctx.root.getOrNull<CommandService>("command")

    if (root != null) {
        return root.fork(ctx)
    }

    return CommandService(ctx, commandRegistry)
}

