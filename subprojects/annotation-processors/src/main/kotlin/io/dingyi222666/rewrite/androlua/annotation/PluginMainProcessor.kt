package io.dingyi222666.rewrite.androlua.annotation


import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toClassName

class PluginMainProcessor(
    private val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    private val services = mutableListOf<Pair<String, KSFile?>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(PluginMain::class.qualifiedName ?: "")

        val filtered = symbols
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        filtered.forEach { it.accept(BuilderVisitor(), Unit) }


        return filtered
    }

    override fun finish() {
        generateConfigFiles()
        super.finish()
    }

    inner class BuilderVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (!classDeclaration.isPublic()) {
                logger.error("Cannot annotate class ${classDeclaration.simpleName.asString()} because it is not public")
                return
            }


            val className = classDeclaration.toClassName().canonicalName

            if (className == "null") {
                logger.error("Cannot annotate class ${classDeclaration.simpleName.asString()} because it is local or anonymous")
                return
            }

            services.add(className to classDeclaration.containingFile)

            logger.info("processing class $className")
        }

    }


    private fun generateConfigFiles() {
        if (services.isEmpty()) {
            return
        }

        val serviceName = "io.dingyi222666.rewrite.androlua.api.plugin.PluginMain"

        val servicePath = ServicesFiles.getPath(serviceName)

        codeGenerator.createNewFile(
            Dependencies(true, *(services.mapNotNull { it.second }.toTypedArray())),
            "",
            servicePath,
            ""
        ).use { stream ->
            ServicesFiles.writeServiceFile(services.map { it.first }, stream)
        }

    }
}

class PluginMainProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return PluginMainProcessor(environment.codeGenerator, environment.logger)
    }
}