package io.dingyi222666.androcode.annotation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName
import io.dingyi222666.androcode.annotation.AutoService


class AutoServiceProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val services = mutableSetOf<AutoServiceInfo>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(AutoService::class.qualifiedName ?: "")

        val autoServiceType =
            resolver
                .getClassDeclarationByName(
                    resolver.getKSNameFromString(
                        AutoService::class.qualifiedName ?: ""
                    )
                )
                ?.asType(emptyList())
                ?: run {
                    val message =
                        "@AutoService type not found on the classpath, skipping processing."

                    logger.warn(message)
                    return emptyList()
                }

        val filtered = symbols
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { function -> function.parent is KSFile || function.qualifiedName?.getQualifier() == function.packageName.getQualifier() }
            .filter {
                it.parameters.size == 1 && it.parameters[0].type.resolve()
                    .toClassName().canonicalName == "io.dingyi222666.androcode.api.context.Context"
            }.toList()


        filtered.forEach { function ->

            val targetAnnotation = function.annotations.find {
                it.annotationType.resolve() == autoServiceType
            }
                ?: run {
                    logger.error(
                        "@AutoService annotation not found",
                        function
                    )
                    return@forEach
                }

            if (targetAnnotation.arguments.size != 2) {
                logger.error(
                    "@AutoService annotation has no target service name",
                    function
                )
                return@forEach
            }

            val targetAnnotationValue =
                (targetAnnotation.arguments[0].value as KSType).toClassName().canonicalName

            var serviceFunctionPrefix =
                function.packageName.asString() + "." + function.containingFile!!.fileName
                    .replaceFirstChar { it.uppercase() }
                    .replace(".kt", "Kt.")

            val jvmNameAnnotation = function.containingFile?.annotations?.find {
                it.annotationType.resolve()
                    .toClassName().canonicalName == JvmName::class.qualifiedName
            }

            if (jvmNameAnnotation != null) {
                serviceFunctionPrefix =
                    function.packageName.asString() + "." + jvmNameAnnotation.arguments[0].value.toString() + "."
            }

            services.add(
                AutoServiceInfo(
                    serviceFile = function.containingFile,
                    serviceFunction = serviceFunctionPrefix + function.simpleName.getShortName(),
                    targetServiceName =
                    targetAnnotationValue
                )
            )
        }

        return emptyList()
    }

    override fun finish() {
        generateConfigFiles()
        super.finish()
    }

    private fun generateConfigFiles() {
        if (services.isEmpty()) {
            return
        }

        services.groupBy {
            it.targetServiceName
        }.forEach { it ->

            logger.info("Generating service file for ${it.value}")

            val services = it.value
            val serviceName = it.key
            val servicePath = ServicesFiles.getPath(serviceName)

            val outputStream = codeGenerator.createNewFile(
                Dependencies(
                    true,
                    *(services.mapNotNull { it.serviceFile }.toTypedArray())
                ),
                "",
                servicePath,
                ""
            )


            ServicesFiles.writeServiceFile(services.map { it.serviceFunction }, outputStream)


            outputStream.close()
        }
    }
}

data class AutoServiceInfo(
    val serviceFunction: String,
    val serviceFile: KSFile?,
    val targetServiceName: String,
)

class AutoServiceProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return AutoServiceProcessor(environment.codeGenerator, environment.logger)
    }
}