plugins {
    `java-library`
}

val mod_version: String by rootProject
val maven_group: String by rootProject

group = maven_group
version = mod_version

tasks.withType<JavaCompile> {
    options.release = 17
}

tasks.withType<Jar> {
    archiveBaseName = "squidcraft-common"
}
