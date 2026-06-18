package io.github.xenfork.squidcraft;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/// @since 26.1.0
public final class SquidCraftBlocks {
    public static final Block COOKED_SHREDDED_SQUID_BLOCK = new Block(BlockBehaviour.Properties.of()
        .setId(SquidCraftBlockItemIds.COOKED_SHREDDED_SQUID_BLOCK.block())
        .mapColor(MapColor.TERRACOTTA_WHITE)
        .instabreak()
        .sound(SoundType.SLIME_BLOCK));

    private SquidCraftBlocks() {
    }

    @FunctionalInterface
    public interface Registry {
        void register(ResourceKey<Block> id, Block block);
    }

    public static void registerAll(Registry registry) {
        registry.register(SquidCraftBlockItemIds.COOKED_SHREDDED_SQUID_BLOCK.block(), COOKED_SHREDDED_SQUID_BLOCK);
    }
}
