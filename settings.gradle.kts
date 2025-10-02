pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}

rootProject.name = "squidcraft"

include("common")
rootDir.resolve("version").listFiles().forEach {
    it.listFiles().forEach { platform ->
        include("version:${it.name}:${platform.name}")
    }
}
