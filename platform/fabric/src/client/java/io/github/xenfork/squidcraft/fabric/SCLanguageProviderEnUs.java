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
public class SCLanguageProviderEnUs extends FabricLanguageProvider {
    protected SCLanguageProviderEnUs(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, @NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add("advancements.squidcraft.root.title", "SquidCraft");
        translationBuilder.add("advancements.squidcraft.root.description", "Get shredded squid");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid.title", "Cooked Shredded Squid");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid.description", "Get cooked shredded squid");
        translationBuilder.add("advancements.squidcraft.glow_shredded_squid.title", "Glow Shredded Squid");
        translationBuilder.add("advancements.squidcraft.glow_shredded_squid.description", "Get glow shredded squid");
        translationBuilder.add("advancements.squidcraft.magma_shredded_squid.title", "Magma Shredded Squid");
        translationBuilder.add("advancements.squidcraft.magma_shredded_squid.description", "Get magma shredded squid");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid_block.title", "Block of Cooked Shredded Squid");
        translationBuilder.add("advancements.squidcraft.cooked_shredded_squid_block.description", "Get block of cooked shredded squid");
        translationBuilder.add(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK, "Block of Cooked Shredded Squid");
        translationBuilder.add(SquidCraftItems.SHREDDED_SQUID, "Shredded Squid");
        translationBuilder.add(SquidCraftItems.COOKED_SHREDDED_SQUID, "Cooked Shredded Squid");
        translationBuilder.add(SquidCraftItems.GLOW_SHREDDED_SQUID, "Glow Shredded Squid");
        translationBuilder.add(SquidCraftItems.MAGMA_SHREDDED_SQUID, "Magma Shredded Squid");
        translationBuilder.add(SquidCraftCreativeModeTabs.MAIN_KEY, "SquidCraft");
        translationBuilder.add("modmenu.nameTranslation.squidcraft", "SquidCraft");
        translationBuilder.add("modmenu.descriptionTranslation.squidcraft", "Make squids useful.");
    }
}
