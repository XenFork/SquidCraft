package io.github.xenfork.squidcraft.neoforge;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @since 0.14.0
 */
public final class RegUtil {
    private RegUtil() {
    }

    public static ResourceLocation id(CommonIdentifier identifier) {
        return ResourceLocation.fromNamespaceAndPath(identifier.namespace(), identifier.path());
    }

    public static <T> T get(Registry<T> registry, CommonIdentifier identifier) {
        return registry.getValue(id(identifier));
    }

    public static Item item(CommonIdentifiable identifiable) {
        return get(BuiltInRegistries.ITEM, identifiable.identifier());
    }

    public static Block block(CommonIdentifiable identifiable) {
        return get(BuiltInRegistries.BLOCK, identifiable.identifier());
    }

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registryKey, CommonIdentifier identifier) {
        return ResourceKey.create(registryKey, id(identifier));
    }

    public static <T> TagKey<T> tagKey(ResourceKey<? extends Registry<T>> registryKey, CommonIdentifier identifier) {
        return TagKey.create(registryKey, id(identifier));
    }
}
