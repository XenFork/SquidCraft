plugins {
    `java-library`
    id("net.neoforged.moddev")
}

val mod_id: String by rootProject
val mod_version: String by rootProject
val minecraft_version: String by rootProject
val neo_version: String by rootProject
val neo_minecraft_version_range: String by rootProject

version = mod_version
group = providers.gradleProperty("maven_group").get()

repositories {
    // Add here additional repositories if required by some of the dependencies below.
}

base {
    archivesName = mod_id
}

java.toolchain.languageVersion = JavaLanguageVersion.of(providers.gradleProperty("required_java_version").get())

neoForge {
    // Specify the version of NeoForge to use.
    version = neo_version

    // This line is optional. Access Transformers are automatically detected
    // accessTransformers = project.files('src/main/resources/META-INF/accesstransformer.cfg')

    // Default run configurations.
    // These can be tweaked, removed, or duplicated as needed.
    runs {
        register("client") {
            client()

            // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        // This run config launches GameTestServer and runs all registered gametests, then exits.
        // By default, the server will crash when no gametests are provided.
        // The gametest system is also enabled by default for other run configs under the /test command.
        register("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        register("data") {
            clientData()

            // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
            // gameDirectory = project.file('run-data')

            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll(
                "--mod",
                mod_id,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }

        // applies to all the run configs above
        configureEach {
            // Recommended logging data for a userdev environment
            // The markers can be added/remove as needed separated by commas.
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            systemProperty("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        // define mod <-> source bindings
        // these are used to tell the game which sources are for which mod
        // multi mod projects should define one per mod
        register(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

// Sets up a dependency configuration called 'localRuntime'.
// This configuration should be used instead of 'runtimeOnly' to declare
// a dependency that will be present for runtime testing but that is
// "optional", meaning it will not be pulled by dependents of this mod.
configurations {
    val localRuntime by registering
    runtimeClasspath.get().extendsFrom(localRuntime)
}

dependencies {
    compileOnly(rootProject)
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
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    var replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to neo_minecraft_version_range,
        "neo_version" to neo_version,
        "mod_id" to mod_id,
        "mod_version" to mod_version,
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}
// Include the output of "generateModMetadata" as an input directory for the build
// this works with both building through Gradle and the IDE.
sourceSets.main.get().resources.srcDir(generateModMetadata)
// To avoid having to run "generateModMetadata" manually, make it run on every project reload
neoForge.ideSyncTask(generateModMetadata)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
}
