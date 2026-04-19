plugins {
    id("net.fabricmc.fabric-loom")
}

val mod_version: String by rootProject
val maven_group: String by rootProject

val minecraft_version: String by rootProject

group = maven_group
version = mod_version

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
}

sourceSets.main {
    resources {
        setSrcDirs(files("src/main/resources", "src/main/generated"))
    }
}
