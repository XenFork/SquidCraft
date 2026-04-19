package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

/// @since 26.1.0
public class SCBlockLootSubProvider extends FabricBlockLootSubProvider {
    protected SCBlockLootSubProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK);
    }
}
