package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

/// @since 26.1.0
public class SCItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public SCItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {
        builder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/raw_fish")))
            .add(SquidCraftItemIds.SHREDDED_SQUID,
                SquidCraftItemIds.GLOW_SHREDDED_SQUID);
        builder(TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("meat")))
            .add(SquidCraftItemIds.SHREDDED_SQUID,
                SquidCraftItemIds.COOKED_SHREDDED_SQUID,
                SquidCraftItemIds.GLOW_SHREDDED_SQUID,
                SquidCraftItemIds.MAGMA_SHREDDED_SQUID);
    }
}
