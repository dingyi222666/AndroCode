@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }
}

rootProject.name = "androcode"
include(":app")


file("subprojects").listFiles()
    ?.forEach {
        val name = it.name
        include(name)
        project(":$name").projectDir = it

    }

file("plugins").listFiles()
    ?.forEach {
        val name = it.name
        include("plugin-$name")
        project(":plugin-$name").projectDir = it
    }

