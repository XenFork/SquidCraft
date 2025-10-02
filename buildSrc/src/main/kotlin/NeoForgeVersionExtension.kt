import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.the

interface NeoForgeVersionExtension {
    val minecraftVersion: Property<String>
    val parchmentMinecraftVersion: Property<String>
    val parchmentMappingsVersion: Property<String>
    val neoVersion: Property<String>
    val javaVersion: Property<Int>
    val minecraftVersionRange: Property<String>
}

fun Project.neoforgeVersion(action: Action<NeoForgeVersionExtension>) {
    action.execute(the<NeoForgeVersionExtension>())
}
