package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftCommon;
import io.github.xenfork.squidcraft.SquidCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/// @since 26.1.0
public class SCAdvancementProvider extends FabricAdvancementProvider {
    protected SCAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.@NonNull Provider registryLookup, @NonNull Consumer<AdvancementHolder> consumer) {
        AdvancementHolder root = Advancement.Builder.advancement()
            .display(
                SquidCraftItems.SHREDDED_SQUID,
                Component.translatable("advancements.squidcraft.root.title"),
                Component.translatable("advancements.squidcraft.root.description"),
                Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                AdvancementType.TASK,
                true,
                false,
                false
            )
            .addCriterion("shredded_squid", InventoryChangeTrigger.TriggerInstance.hasItems(SquidCraftItems.SHREDDED_SQUID))
            .save(consumer, SquidCraftCommon.MOD_ID + ":root");

        AdvancementHolder cooked_shredded_squid = Advancement.Builder.advancement()
            .parent(root)
            .display(
                SquidCraftItems.COOKED_SHREDDED_SQUID,
                Component.translatable("advancements.squidcraft.cooked_shredded_squid.title"),
                Component.translatable("advancements.squidcraft.cooked_shredded_squid.description"),
                null,
                AdvancementType.TASK,
                true,
                false,
                false
            )
            .addCriterion("cooked_shredded_squid", InventoryChangeTrigger.TriggerInstance.hasItems(SquidCraftItems.COOKED_SHREDDED_SQUID))
            .save(consumer, SquidCraftCommon.MOD_ID + ":cooked_shredded_squid");

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                SquidCraftItems.GLOW_SHREDDED_SQUID,
                Component.translatable("advancements.squidcraft.glow_shredded_squid.title"),
                Component.translatable("advancements.squidcraft.glow_shredded_squid.description"),
                null,
                AdvancementType.TASK,
                true,
                false,
                false
            )
            .addCriterion("glow_shredded_squid", InventoryChangeTrigger.TriggerInstance.hasItems(SquidCraftItems.GLOW_SHREDDED_SQUID))
            .save(consumer, SquidCraftCommon.MOD_ID + ":glow_shredded_squid");

        Advancement.Builder.advancement()
            .parent(cooked_shredded_squid)
            .display(
                SquidCraftItems.MAGMA_SHREDDED_SQUID,
                Component.translatable("advancements.squidcraft.magma_shredded_squid.title"),
                Component.translatable("advancements.squidcraft.magma_shredded_squid.description"),
                null,
                AdvancementType.TASK,
                true,
                false,
                false
            )
            .addCriterion("magma_shredded_squid", InventoryChangeTrigger.TriggerInstance.hasItems(SquidCraftItems.MAGMA_SHREDDED_SQUID))
            .save(consumer, SquidCraftCommon.MOD_ID + ":magma_shredded_squid");

        Advancement.Builder.advancement()
            .parent(cooked_shredded_squid)
            .display(
                SquidCraftItems.COOKED_SHREDDED_SQUID_BLOCK,
                Component.translatable("advancements.squidcraft.cooked_shredded_squid_block.title"),
                Component.translatable("advancements.squidcraft.cooked_shredded_squid_block.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .addCriterion("cooked_shredded_squid_block", InventoryChangeTrigger.TriggerInstance.hasItems(SquidCraftItems.COOKED_SHREDDED_SQUID_BLOCK))
            .save(consumer, SquidCraftCommon.MOD_ID + ":cooked_shredded_squid_block");
    }
}
