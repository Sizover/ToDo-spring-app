rootProject.name = "testing-e2e"

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
