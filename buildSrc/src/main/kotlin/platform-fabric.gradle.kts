import net.fabricmc.loom.task.AbstractRemapJarTask

plugins {
    id("fabric-loom")
}

val mod_id: String by rootProject
val mod_version: String by rootProject
val maven_group: String by rootProject

val fabricVersion = project.extensions.create<FabricVersionExtension>("fabricVersion")

group = maven_group
version = mod_version

repositories {
    maven("https://maven.shedaniel.me/")
}

base {
    archivesName = mod_id
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("squidcraft") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets["client"])
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    implementation(project(":common"))

    minecraft(fabricVersion.minecraftVersion.map { "com.mojang:minecraft:$it" })
    mappings(fabricVersion.mappingsVersion.map { "net.fabricmc:yarn:$it:v2" })
    modImplementation(fabricVersion.fabricLoaderVersion.map { "net.fabricmc:fabric-loader:$it" })
    modImplementation(fabricVersion.fabricApiVersion.map { "net.fabricmc.fabric-api:fabric-api:$it" })

    modRuntimeOnly(fabricVersion.reiVersion.map { "me.shedaniel:RoughlyEnoughItems-fabric:$it" })
}

val copyCommonCode = tasks.register<Copy>("copyCommonCode") {
    dependsOn(project(":common").tasks["classes"])

    from(project(":common").layout.buildDirectory.file("classes/java/main"))
    into(layout.buildDirectory.file("classes/java/main"))
}

val copyCommonRes = tasks.register<Copy>("copyCommonRes") {
    dependsOn(project(":common").tasks["classes"])

    from(project(":common").layout.buildDirectory.file("resources/main"))
    into(layout.buildDirectory.file("resources/main"))
}

tasks.named("compileJava") {
    dependsOn(copyCommonCode)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(copyCommonRes)

    val replaceProperties = mapOf(
        "version" to project.version,
        "required_minecraft_version" to fabricVersion.requiredMinecraftVersion,
        "required_java_version" to fabricVersion.javaVersion
    )

    inputs.properties(replaceProperties)
    filesMatching("fabric.mod.json") {
        expand(replaceProperties)
    }
}

tasks.withType<JavaCompile> {
    options.release = fabricVersion.javaVersion
}

java {
    withSourcesJar()
}

fun AbstractArchiveTask.addVersionSuffix() {
    archiveVersion = fabricVersion.minecraftVersion.map { "$version-$it-fabric" }
}

tasks.withType<Jar> {
    addVersionSuffix()
}

tasks.withType<AbstractRemapJarTask> {
    addVersionSuffix()
}

tasks.named<Jar>("jar") {
    inputs.property("archivesName", project.base.archivesName)

    from(rootDir.resolve("LICENSE")) {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

afterEvaluate {
    java {
        val v = JavaVersion.toVersion(fabricVersion.javaVersion.get())
        sourceCompatibility = v
        targetCompatibility = v
    }
}
