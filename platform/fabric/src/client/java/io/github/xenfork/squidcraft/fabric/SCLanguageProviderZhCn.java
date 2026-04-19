package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftBlocks;
import io.github.xenfork.squidcraft.SquidCraftCreativeModeTabs;
import io.github.xenfork.squidcraft.SquidCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

/// @since 26.1.0
public class SCLanguageProviderZhCn extends FabricLanguageProvider {
    protected SCLanguageProviderZhCn(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, @NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add("advancements.squidcraft.root.title", "鱿鱼工艺");
        translationBuilder.add("advancements.squidcraft.root.description", "获得鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid.title", "熟鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid.description", "获得熟鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.glow_shredded_squid.title", "荧光鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.glow_shredded_squid.description", "获得荧光鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.magma_shredded_squid.title", "岩浆鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.magma_shredded_squid.description", "获得岩浆鱿鱼丝");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid_block.title", "熟鱿鱼丝块");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid_block.description", "获得熟鱿鱼丝块");
        translationBuilder.add(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK, "熟鱿鱼丝块");
        translationBuilder.add(SquidCraftItems.SHREDDED_SQUID, "鱿鱼丝");
        translationBuilder.add(SquidCraftItems.COOKED_SHREDDED_SQUID, "熟鱿鱼丝");
        translationBuilder.add(SquidCraftItems.GLOW_SHREDDED_SQUID, "荧光鱿鱼丝");
        translationBuilder.add(SquidCraftItems.MAGMA_SHREDDED_SQUID, "岩浆鱿鱼丝");
        translationBuilder.add(SquidCraftCreativeModeTabs.MAIN_KEY, "鱿鱼工艺");
        translationBuilder.add("modmenu.nameTranslation.squidcraft", "鱿鱼工艺");
        translationBuilder.add("modmenu.descriptionTranslation.squidcraft", "让鱿鱼更有用");
    }
}
