package io.dingyi222666.rewrite.androlua.api.plugin

import io.dingyi222666.rewrite.androlua.api.command.CommandDescriptor


data class PluginConfig(
    val id: String,
    val displayName: String,
    val version: String,
    val apiDependencyVersion: Int,
    val dependencyPlugins: List<DependencyPlugin>,
    val commands: List<CommandDescriptor>,
    val activationEvents: List<String>
)

data class DependencyPlugin(
    val id: String,
    val version: String
)


fun buildPluginConfig(block: PluginConfigBuilder.() -> Unit): PluginConfig {
    return PluginConfigBuilder().apply(block).build()
}

class PluginConfigBuilder {
    var id: String = ""
    var displayName = id
    var version = "1.0.0"
    var apiDependencyVersion: Int = 0
    private var dependencyPlugins: List<DependencyPlugin> = emptyList()
    private var commands: List<CommandDescriptor> = emptyList()
    private var activationEvents: List<String> = emptyList()

    fun build(): PluginConfig {

        if (id.isEmpty()) {
            throw IllegalArgumentException("Plugin id is empty")
        }

        return PluginConfig(
            id,
            displayName,
            version,
            apiDependencyVersion,
            dependencyPlugins,
            commands,
            activationEvents
        )
    }

    fun dependencyPlugins(block: DependencyPluginBuilder.() -> Unit) {
        dependencyPlugins = DependencyPluginBuilder().apply(block).build()
    }

    fun commands(block: CommandDescriptorBuilder.() -> Unit) {
        commands = CommandDescriptorBuilder().apply(block).build()
    }

    fun activationEvents(block: ActivationEventBuilder.() -> Unit) {
        activationEvents = ActivationEventBuilder().apply(block).build()
    }

    fun activationEvents(vararg events: String) {
        activationEvents = events.toList()
    }


    inner class ActivationEventBuilder {

        private var events: MutableList<String> = mutableListOf()

        infix fun event(event: String) {
            events += event
        }

        fun events(vararg events: String) {
            this.events += events
        }

        fun build() = events
    }


    inner class CommandDescriptorBuilder {
        private var commands: MutableList<CommandDescriptor> = mutableListOf()

        fun command(id: String, title: String, description: String) {
            commands += CommandDescriptor(id, title, description)
        }

        fun build() = commands
    }

    inner class DependencyPluginBuilder {
        private var dependencyPlugins: MutableList<DependencyPlugin> =
            mutableListOf()

        fun plugin(id: String, version: String) {
            dependencyPlugins += DependencyPlugin(id, version)
        }


        fun build() = dependencyPlugins
    }
}