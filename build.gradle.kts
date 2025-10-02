plugins {
    id("java")
}

val mod_version: String by rootProject
val maven_group: String by rootProject

group = maven_group
version = mod_version

repositories {
    mavenCentral()
}
