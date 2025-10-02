package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * @since 0.14.0
 */
public final class RegUtil {
    private RegUtil() {
    }

    public static Identifier id(CommonIdentifier identifier) {
        return new Identifier(identifier.namespace(), identifier.path());
    }

    public static <T> T get(Registry<T> registry, CommonIdentifier identifier) {
        return registry.get(id(identifier));
    }

    public static Item item(CommonIdentifiable identifiable) {
        return get(Registries.ITEM, identifiable.identifier());
    }

    public static Block block(CommonIdentifiable identifiable) {
        return get(Registries.BLOCK, identifiable.identifier());
    }

    public static <T> RegistryKey<T> key(RegistryKey<? extends Registry<T>> registryKey, CommonIdentifier identifier) {
        return RegistryKey.of(registryKey, id(identifier));
    }

    public static <T> TagKey<T> tagKey(RegistryKey<? extends Registry<T>> registryKey, CommonIdentifier identifier) {
        return TagKey.of(registryKey, id(identifier));
    }
}
