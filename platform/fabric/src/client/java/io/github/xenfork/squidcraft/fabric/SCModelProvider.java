package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftBlocks;
import io.github.xenfork.squidcraft.SquidCraftItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import org.jspecify.annotations.NonNull;

/// @since 26.1.0
public class SCModelProvider extends FabricModelProvider {
    public SCModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK);
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(SquidCraftItems.SHREDDED_SQUID, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(SquidCraftItems.COOKED_SHREDDED_SQUID, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(SquidCraftItems.GLOW_SHREDDED_SQUID, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(SquidCraftItems.MAGMA_SHREDDED_SQUID, ModelTemplates.FLAT_ITEM);
    }
}
