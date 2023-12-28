plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlinKsp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.kotlin.ksp.api)
    implementation(projects.ideAnnotation)
    implementation(libs.square.kotlin.poet)
    implementation(libs.square.kotlin.poet.ksp)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}