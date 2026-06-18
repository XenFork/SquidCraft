package io.github.xenfork.squidcraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/// @since 26.2.0
public final class SquidCraftItemIds {
    public static final ResourceKey<Item> SHREDDED_SQUID = resourceKey("shredded_squid");
    public static final ResourceKey<Item> COOKED_SHREDDED_SQUID = resourceKey("cooked_shredded_squid");
    public static final ResourceKey<Item> GLOW_SHREDDED_SQUID = resourceKey("glow_shredded_squid");
    public static final ResourceKey<Item> MAGMA_SHREDDED_SQUID = resourceKey("magma_shredded_squid");
    public static final ResourceKey<Item> COOKED_SHREDDED_SQUID_BLOCK = resourceKey("cooked_shredded_squid_block");

    private SquidCraftItemIds() {
    }

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, SquidCraftCommon.id(name));
    }
}
