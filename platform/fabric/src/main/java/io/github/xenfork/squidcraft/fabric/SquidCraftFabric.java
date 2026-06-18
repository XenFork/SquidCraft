package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

/// @since 0.15.0
public class SquidCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SquidCraftBlocks.registerAll((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
        SquidCraftItems.registerAll((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SquidCraftCreativeModeTabs.MAIN_KEY, creativeModeTab(SquidCraftCreativeModeTabs.MAIN));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
            .register(output -> SquidCraftCreativeModeTabs.addFoods(output::accept));
        registerLootTables();
    }

    private static CreativeModeTab creativeModeTab(CommonCreativeModeTab commonCreativeModeTab) {
        return FabricCreativeModeTab.builder()
            .title(commonCreativeModeTab.title)
            .icon(commonCreativeModeTab.icon)
            .displayItems((parameters, output) ->
                commonCreativeModeTab.displayItems.accept(parameters, output::accept))
            .build();
    }

    private static void registerLootTables() {
        LootTableEvents.MODIFY_DROPS.register((holder, context, drops) -> {
            Optional<ResourceKey<LootTable>> squidLootTable = EntityTypes.SQUID.getDefaultLootTable();
            if (squidLootTable.isPresent() && holder.is(squidLootTable.get())) {
                addLootItem(context, drops, SquidCraftLootTables.SHREDDED_SQUID);
            } else {
                Optional<ResourceKey<LootTable>> glowSquidLootTable = EntityTypes.GLOW_SQUID.getDefaultLootTable();
                if (glowSquidLootTable.isPresent() && holder.is(glowSquidLootTable.get())) {
                    addLootItem(context, drops, SquidCraftLootTables.GLOW_SHREDDED_SQUID);
                }
            }
        });
    }

    private static void addLootItem(LootContext context, List<ItemStack> drops, ResourceKey<LootTable> key) {
        context.getResolver().lookup(Registries.LOOT_TABLE)
            .flatMap(reg -> reg.get(key))
            .map(Holder.Reference::value)
            .ifPresentOrElse(
                lootTable -> lootTable.getRandomItems(context, drops::add),
                () -> SquidCraftCommon.LOGGER.warn("Unable to find loot table: {}", key.identifier())
            );
    }
}
