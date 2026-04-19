package io.github.xenfork.squidcraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

/// @since 26.1.0
public final class SquidCraftLootTables {
    public static final ResourceKey<LootTable> SHREDDED_SQUID = ResourceKey.create(Registries.LOOT_TABLE, SquidCraftCommon.id("shredded_squid"));
    public static final ResourceKey<LootTable> GLOW_SHREDDED_SQUID = ResourceKey.create(Registries.LOOT_TABLE, SquidCraftCommon.id("glow_shredded_squid"));

    private SquidCraftLootTables() {
    }
}
