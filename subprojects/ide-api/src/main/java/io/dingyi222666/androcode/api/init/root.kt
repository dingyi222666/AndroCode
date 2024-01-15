package io.dingyi222666.androcode.api.init


import androidx.activity.ComponentActivity
import io.dingyi222666.androcode.annotation.AutoGenerateServiceExtension
import io.dingyi222666.androcode.annotation.AutoService
import io.dingyi222666.androcode.api.AndroCodeContext
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipFile

class InitService(override val ctx: Context) : Service {
    override val id = "init"

    lateinit var appWorkDirectory: File

    private var isInit = false

    suspend fun start(
        androidContext: ComponentActivity,
        statusFlow: MutableSharedFlow<InitStatus>
    ) {

        if (isInit) {
            throw IllegalStateException("The init service has been initialized")
        }

        // unzip apk files
        statusFlow.emit(
            InitStatus(
                formattedMessage = "unzip apk files"
            )
        )

        unzipApkFiles(androidContext, statusFlow)


        // TODO: load plugin
    }

    private suspend fun unzipApkFiles(
        androidContext: ComponentActivity,
        statusFlow: MutableSharedFlow<InitStatus>
    ) =
        withContext(Dispatchers.IO) {
            val targetPath = androidContext.getExternalFilesDir("")?.parentFile
                ?: throw IllegalStateException("The external files directory is null")

            appWorkDirectory = targetPath


            val currentApkPath = androidContext.packageResourcePath

            // unzip to targetPath

            val zipFile = ZipFile(currentApkPath)

            for (zipEntry in zipFile.entries()) {
                val entryName = zipEntry.name

                if (!entryName.startsWith("assets/")) {
                    continue
                }

                val entryPath = targetPath.path + File.separator + entryName.replace("assets/", "")

                statusFlow.emit(
                    InitStatus(
                        currentZipFile = entryName,
                        formattedMessage = "uncompress $entryName"
                    )
                )

                if (zipEntry.isDirectory) {
                    File(entryPath).mkdirs()
                } else {
                    val entryFile = File(entryPath)
                    entryFile.parentFile?.mkdirs()
                    zipFile.getInputStream(zipEntry).use { input ->
                        entryFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }

            zipFile.close()

        }

    override fun dispose() {
        super.dispose()
    }
}

@AutoService(Context::class, "init")
@AutoGenerateServiceExtension(Context::class, "init", "init")
fun createInitService(ctx: Context): InitService {
    return ctx.root.getOrNull("init", false) ?: InitService(ctx)
}


data class InitStatus(
    val currentZipFile: String? = null,
    val currentLoadPluginConfig: String? = null,
    val error: Throwable? = null,
    val formattedMessage: String
)