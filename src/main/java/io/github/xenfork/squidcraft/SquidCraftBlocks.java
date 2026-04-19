package io.github.xenfork.squidcraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/// @since 26.1.0
public final class SquidCraftBlocks {
    public static final Identifier COOKED_SHREDDED_SQUID_BLOCK_ID = SquidCraftCommon.id("cooked_shredded_squid_block");
    public static final Block COOKED_SHREDDED_SQUID_BLOCK = new Block(BlockBehaviour.Properties.of()
        .setId(ResourceKey.create(Registries.BLOCK, COOKED_SHREDDED_SQUID_BLOCK_ID))
        .mapColor(MapColor.TERRACOTTA_WHITE)
        .instabreak()
        .sound(SoundType.SLIME_BLOCK));

    private SquidCraftBlocks() {
    }

    @FunctionalInterface
    public interface Registry {
        void register(Identifier id, Block block);
    }

    public static void registerAll(Registry registry) {
        registry.register(COOKED_SHREDDED_SQUID_BLOCK_ID, COOKED_SHREDDED_SQUID_BLOCK);
    }
}
