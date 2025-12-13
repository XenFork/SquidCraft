package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * @since 0.14.0
 */
public final class FabricBackend implements Backend {
    @Override
    public void registerBlock(CommonBlock block) {
        CommonIdentifier identifier = block.identifier();

        BlockBehaviour.Properties settings = BlockBehaviour.Properties.of();
        settings.mapColor(switch (block.mapColor()) {
            case TERRACOTTA_WHITE -> MapColor.TERRACOTTA_WHITE;
        });
        if (block.instantBreak()) {
            settings.instabreak();
        }
        settings.sound(switch (block.soundType()) {
            case SLIME_BLOCK -> SoundType.SLIME_BLOCK;
        });

        Registry.register(BuiltInRegistries.BLOCK,
            new ResourceLocation(identifier.namespace(), identifier.path()),
            new Block(settings));
    }

    @Override
    public void registerItem(CommonItem item) {
        CommonIdentifier identifier = item.identifier();
        CommonFoodComponent commonFoodComponent = item.foodComponent();
        CommonConsumableComponent consumableComponent = item.consumableComponent();
        CommonBlock commonBlock = item.itemBlock();

        Item.Properties settings = new Item.Properties();
        if (commonFoodComponent != null) {
            FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(commonFoodComponent.nutrition())
                .saturationMod(commonFoodComponent.saturationModifier());
            if (commonFoodComponent.meat()) {
                builder.meat();
            }
            if (commonFoodComponent.alwaysEat()) {
                builder.alwaysEat();
            }
            if (consumableComponent != null) {
                if (consumableComponent.fast()) {
                    builder.fast();
                }
                for (var entry : consumableComponent.effects()) {
                    CommonEffectInstance instance = entry.getKey();
                    float chance = entry.getValue();
                    builder.effect(new MobEffectInstance(switch (instance.effect()) {
                        case FIRE_RESISTANCE -> MobEffects.FIRE_RESISTANCE;
                        case GLOWING -> MobEffects.GLOWING;
                    }, instance.durationTicks(), instance.amplifier()), chance);
                }
            }
            settings.food(builder.build());
        }

        Item item1;
        if (commonBlock != null) {
            item1 = new BlockItem(RegUtil.block(commonBlock), settings);
        } else {
            item1 = new Item(settings);
        }
        Registry.register(BuiltInRegistries.ITEM,
            new ResourceLocation(identifier.namespace(), identifier.path()),
            item1);
    }

    @Override
    public void registerCreativeTab(CommonCreativeTab creativeTab) {
        CommonIdentifier identifier = creativeTab.identifier();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(identifier.namespace(), identifier.path()),
            FabricItemGroup.builder()
                .title(Component.translatable(creativeTab.titleTranslationKey()))
                .icon(() -> new ItemStack(RegUtil.item(creativeTab.icon())))
                .displayItems((displayContext, entries) -> {
                    for (ICommonItem displayItem : creativeTab.displayItems()) {
                        entries.accept(RegUtil.item(displayItem));
                    }
                })
                .build());
    }

    @Override
    public void registerLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (source.isBuiltin()) {
                if (EntityType.SQUID.getDefaultLootTable().equals(id)) {
                    addLootItem(builder, RegUtil.item(CommonItems.SHREDDED_SQUID));
                } else if (EntityType.GLOW_SQUID.getDefaultLootTable().equals(id)) {
                    addLootItem(builder, RegUtil.item(CommonItems.GLOW_SHREDDED_SQUID));
                }
            }
        });
    }

    private static void addLootItem(LootTable.Builder builder, Item item) {
        builder.withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 8f)))
                .apply(SmeltItemFunction.smelted().when(() ->
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                            .flags(EntityFlagsPredicate.Builder.flags()
                                .setOnFire(true)
                                .build())
                    ).build())
                )
                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1f, 4f)))
            )
        );
    }
}
