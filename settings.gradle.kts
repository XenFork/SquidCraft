pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version providers.gradleProperty("fabric_loom_version")
        id("net.neoforged.moddev") version providers.gradleProperty("neoforged_moddev_version")
    }
}

rootProject.name = "squidcraft"

include("platform:fabric")
include("platform:neoforge")
