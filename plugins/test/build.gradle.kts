import org.gradle.configurationcache.extensions.capitalized

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKsp)
}

android {
    namespace = "io.dingyi222666.androcode.plugin.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.dingyi222666.androcode.plugin.test"
        minSdk = 26
        //noinspection EditedTargetSdkVersion,ExpiredTargetSdkVersion
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    applicationVariants.all {
        logger.lifecycle("Configure application variant $name")

        val appProject = project(":app")

        val outputFileDir =
            "${appProject.projectDir}/src/main/assets/plugins"

        val path = project.name + "-" +buildType.name + "-" +
                versionName + ".apk"

        outputs
            // default type don't have outputFileName field
            .map { it as com.android.build.gradle.internal.api.ApkVariantOutputImpl }
            .all { output ->
                output.outputFileName = path
                false
            }

        appProject.getTasksByName("pre${name.capitalized()}Build", true).forEach {
            it.apply {
                dependsOn(this@all.assembleProvider.get())
            }
        }

        assembleProvider.configure {
            // enabled = true

            doLast {
                copy {
                    this@all.outputs.forEach { file ->
                        copy {
                            from(file.outputFile)
                            into(outputFileDir)
                        }
                    }
                }
            }
        }

    }
}

logger.lifecycle("Configure project ${project.name}")

dependencies {
    compileOnly(projects.ideApi)
    implementation(projects.ideAnnotation)
    ksp(projects.annotationProcessors)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}