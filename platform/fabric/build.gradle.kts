plugins {
    id("net.fabricmc.fabric-loom")
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

val mod_id: String by rootProject
val required_java_version: String by rootProject
val minecraft_version: String by rootProject
val fabric_minecraft_version_range: String by rootProject

base {
    archivesName = mod_id
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("squidcraft") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
        outputDirectory = rootDir.resolve("src/main/generated")
    }
}

repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven("https://maven.ryanliptak.com/")
}

dependencies {
    compileOnly(rootProject)
    minecraft("com.mojang:minecraft:$minecraft_version")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("fabric_loader_version").get()}")
    implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

    implementation("com.terraformersmc:modmenu:${providers.gradleProperty("modmenu_version").get()}")
    runtimeOnly("squeek.appleskin:appleskin-fabric:${providers.gradleProperty("fabric_appleskin_version").get()}")
}

val copyCommonCode = tasks.register<Copy>("copyCommonCode") {
    dependsOn(rootProject.tasks["classes"])

    from(rootProject.layout.buildDirectory.file("classes/java/main"))
    into(layout.buildDirectory.file("classes/java/main"))
}

val copyCommonRes = tasks.register<Copy>("copyCommonRes") {
    dependsOn(rootProject.tasks["classes"])

    from(rootProject.layout.buildDirectory.file("resources/main"))
    into(layout.buildDirectory.file("resources/main"))
}

tasks.compileJava {
    dependsOn(copyCommonCode)
}

tasks.processResources {
    dependsOn(copyCommonRes)

    inputs.property("version", version)
    inputs.property("minecraft_version_range", fabric_minecraft_version_range)
    inputs.property("required_java_version", required_java_version)

    filesMatching("fabric.mod.json") {
        expand(
            "version" to version,
            "minecraft_version_range" to fabric_minecraft_version_range,
            "required_java_version" to required_java_version
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = required_java_version.toInt()
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.toVersion(required_java_version)
    targetCompatibility = JavaVersion.toVersion(required_java_version)
}

tasks.withType<Jar> {
    archiveVersion = "$version-fabric"
}

tasks.jar {
    val projectName = project.name
    inputs.property("projectName", projectName)

    from(rootDir.resolve("LICENSE")) {
        rename { "${it}_$projectName" }
    }
}
