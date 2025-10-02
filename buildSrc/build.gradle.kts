plugins {
    `kotlin-dsl`
}

val loom_version: String by rootProject
val moddev_version: String by rootProject

repositories {
    maven("https://maven.fabricmc.net/")
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("fabric-loom:fabric-loom.gradle.plugin:$loom_version")
    implementation("net.neoforged.moddev:net.neoforged.moddev.gradle.plugin:${moddev_version}")
}
