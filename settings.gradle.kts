@file:Suppress("UnstableApiUsage")

include(":ide-annotation")



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

rootProject.name = "rewrite-androlua"
include(":app")

//foreach platform
file("subprojects").listFiles()
    ?.forEach {
        val name = it.name
        include(name)
        project(":$name").projectDir = it

    }
