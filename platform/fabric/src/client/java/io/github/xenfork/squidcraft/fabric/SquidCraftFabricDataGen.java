package io.github.xenfork.squidcraft.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/// @since 0.15.0
public class SquidCraftFabricDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SCLanguageProviderEnUs::new);
        pack.addProvider(SCLanguageProviderZhCn::new);
        pack.addProvider(SCItemTagsProvider::new);
        pack.addProvider(SCBlockLootSubProvider::new);
        pack.addProvider(SCModelProvider::new);
        pack.addProvider(SCRecipeProvider::new);
        pack.addProvider(SCAdvancementProvider::new);
        pack.addProvider(SCLootTableProvider::new);
    }
}
