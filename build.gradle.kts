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

tasks.register("runAllDataGen") {
    subprojects.forEach { project ->
        val tasks = project.tasks
        tasks.findByName("runDatagen")?.also { dependsOn(it) }
        tasks.findByName("runData")?.also { dependsOn(it) }
    }
}
