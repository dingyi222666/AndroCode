package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.command.CommandDescriptor


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
    var packageName: String = ""
    var displayName = packageName
    var version = "1.0.0"
    var sdkVersion: Int = 0
    private var dependencyPlugins: List<DependencyPlugin> = emptyList()
    private var commands: List<CommandDescriptor> = emptyList()
    private var activationEvents: List<String> = emptyList()

    fun build(): PluginConfig {

        if (packageName.isEmpty()) {
            throw IllegalArgumentException("Plugin id is empty")
        }

        return PluginConfig(
            packageName,
            displayName,
            version,
            sdkVersion,
            dependencyPlugins,
            commands,
            activationEvents
        )
    }

    fun dependencies(block: DependencyPluginBuilder.() -> Unit) {
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

        fun plugin(all: String) {
            val ids = all.split(":")
            if (ids.size != 2) {
                throw IllegalArgumentException("Invalid plugin id: $all")
            }
            dependencyPlugins += DependencyPlugin(ids[0], ids[1])

        }

        fun build() = dependencyPlugins
    }
}