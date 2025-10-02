import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.the

interface FabricVersionExtension {
    val minecraftVersion: Property<String>
    val mappingsVersion: Property<String>
    val fabricLoaderVersion: Property<String>
    val fabricApiVersion: Property<String>
    val javaVersion: Property<Int>
    val requiredMinecraftVersion: Property<String>
    val reiVersion: Property<String>
}

fun Project.fabricVersion(action: Action<FabricVersionExtension>) {
    action.execute(the<FabricVersionExtension>())
}
