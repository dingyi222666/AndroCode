package io.dingyi222666.rewrite.androlua.annotation

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
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo


class AutoGenerateServiceExtensionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val generatedList = mutableSetOf<AutoGenerateServiceExtensionInfo>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(
            AutoGenerateServiceExtension::class.qualifiedName ?: ""
        )

        val autoServiceType =
            resolver
                .getClassDeclarationByName(
                    resolver.getKSNameFromString(
                        AutoGenerateServiceExtension::class.qualifiedName ?: ""
                    )
                )
                ?.asType(emptyList())
                ?: run {
                    val message =
                        "@AutoGenerateServiceExtension type not found on the classpath, skipping processing."

                    logger.error(message)
                    return emptyList()
                }

        symbols
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { function -> function.parent is KSFile || function.qualifiedName?.getQualifier() == function.packageName.getQualifier() }
            .filter {
                it.parameters.size == 1 && it.parameters[0].type.resolve()
                    .toClassName().canonicalName == "io.dingyi222666.rewrite.androlua.api.context.Context"
            }
            .forEach { function ->

                val targetAnnotation = function.annotations.find {
                    it.annotationType.resolve() == autoServiceType
                }
                    ?: run {
                        logger.error(
                            "@AutoGenerateServiceExtension annotation not found",
                            function
                        )
                        return@forEach
                    }

                if (targetAnnotation.arguments.size != 3) {
                    logger.error(
                        "@AutoGenerateServiceExtension annotation has no target service name",
                        function
                    )
                    return@forEach
                }

                val targetAnnotationClassValue =
                    (targetAnnotation.arguments[0].value as KSType).toClassName().canonicalName

                val targetAnnotationIdValue = targetAnnotation.arguments[1].value.toString()

                val targetAnnotationVariableName = targetAnnotation.arguments[2].value.toString()

                generatedList.add(
                    AutoGenerateServiceExtensionInfo(
                        serviceFile = function.containingFile!!,
                        serviceId = targetAnnotationIdValue,
                        serviceType = function.returnType?.resolve()?.toClassName()?.canonicalName
                            ?: run {
                                logger.warn(
                                    "@AutoGenerateServiceExtension has no return service type",
                                    function
                                )
                                return@forEach
                            },
                        variableName = targetAnnotationVariableName,
                        targetServiceName = targetAnnotationClassValue
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
        if (generatedList.isEmpty()) {
            return
        }

        generatedList.groupBy {
            it.targetServiceName
        }.forEach { it ->

            val services = it.value
            val first = it.value.first()

            val packageName = first.serviceFile.packageName.getQualifier()
            val className = first.serviceFile.fileName.replace(".kt", "ServiceExtensions")

            generateKotlinFile(packageName, className, codeGenerator, services)

        }

    }

    private fun generateKotlinFile(
        packageName: String,
        className: String,
        codeGenerator: CodeGenerator,
        services: List<AutoGenerateServiceExtensionInfo>
    ) {


        val file = FileSpec.builder(packageName, className)
            .addImport("io.dingyi222666.rewrite.androlua.api.context", "getAs")
            .apply {
                services.forEach { service ->


                    val serviceType = ClassName(
                        service.serviceType.substringBeforeLast('.'),
                        service.serviceType.substringAfterLast('.')
                    )

                    addProperty(
                        // val command
                        PropertySpec.builder(
                            service.variableName,
                            serviceType,
                            KModifier.PUBLIC
                        )
                            // val Context.command
                            .receiver(
                                ClassName(
                                    service.targetServiceName.substringBeforeLast('.'),
                                    service.targetServiceName.substringAfterLast('.')
                                )
                            )
                            // val Context.command
                            // get() =
                            .getter(
                                // getAs<Service>("command")
                                FunSpec.getterBuilder()
                                    .addStatement(
                                        "return getAs<%T>(%S)", serviceType, service.serviceId
                                    )
                                    .build()
                            )
                            .build()
                    )
                }
            }
            .build()

        file.writeTo(
            codeGenerator,
            Dependencies(true, *(services.map { it.serviceFile }.toTypedArray()))
        )
    }

}

data class AutoGenerateServiceExtensionInfo(
    val serviceId: String,
    val variableName: String,
    val serviceFile: KSFile,
    val serviceType: String,
    val targetServiceName: String,
)

class AutoGenerateServiceExtensionProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return AutoGenerateServiceExtensionProcessor(environment.codeGenerator, environment.logger)
    }
}