package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

/**
 * @since 0.14.0
 */
public final class FabricBackend implements Backend {
    @Override
    public void registerBlock(CommonBlock block) {
        CommonIdentifier identifier = block.identifier();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(identifier.namespace(), identifier.path());

        BlockBehaviour.Properties settings = BlockBehaviour.Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, id));
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
            id,
            new Block(settings));
    }

    @Override
    public void registerItem(CommonItem item) {
        CommonIdentifier identifier = item.identifier();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(identifier.namespace(), identifier.path());
        CommonFoodComponent commonFoodComponent = item.foodComponent();
        CommonConsumableComponent commonConsumableComponent = item.consumableComponent();
        CommonBlock commonBlock = item.itemBlock();

        Item.Properties settings = new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id));
        if (commonFoodComponent != null) {
            FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(commonFoodComponent.nutrition())
                .saturationModifier(commonFoodComponent.saturationModifier());
            if (commonFoodComponent.alwaysEat()) {
                builder.alwaysEdible();
            }
            settings.food(builder.build());
        }
        if (commonConsumableComponent != null) {
            Consumable.Builder builder = Consumable.builder();
            if (commonConsumableComponent.fast()) {
                builder.consumeSeconds(0.8f);
            }
            for (var entry : commonConsumableComponent.effects()) {
                CommonEffectInstance instance = entry.getKey();
                float chance = entry.getValue();
                builder.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(switch (instance.effect()) {
                    case FIRE_RESISTANCE -> MobEffects.FIRE_RESISTANCE;
                    case GLOWING -> MobEffects.GLOWING;
                }, instance.durationTicks(), instance.amplifier()), chance));
            }
            settings.component(DataComponents.CONSUMABLE, builder.build());
        }

        Item item1;
        if (commonBlock != null) {
            settings.useBlockDescriptionPrefix();
            item1 = new BlockItem(RegUtil.block(commonBlock), settings);
        } else {
            item1 = new Item(settings);
        }
        Registry.register(BuiltInRegistries.ITEM,
            id,
            item1);
    }

    @Override
    public void registerCreativeTab(CommonCreativeTab creativeTab) {
        CommonIdentifier identifier = creativeTab.identifier();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(identifier.namespace(), identifier.path()),
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
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (source.isBuiltin()) {
                if (EntityType.SQUID.getDefaultLootTable().isPresent() && EntityType.SQUID.getDefaultLootTable().get().equals(key)) {
                    addLootItem(builder, registries, RegUtil.item(CommonItems.SHREDDED_SQUID));
                } else if (EntityType.GLOW_SQUID.getDefaultLootTable().isPresent() && EntityType.GLOW_SQUID.getDefaultLootTable().get().equals(key)) {
                    addLootItem(builder, registries, RegUtil.item(CommonItems.GLOW_SHREDDED_SQUID));
                }
            }
        });
    }

    private static void addLootItem(LootTable.Builder builder, HolderLookup.Provider registries, Item item) {
        builder.withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 8f)))
                .apply(SmeltItemFunction.smelted().when(() ->
                    AnyOfCondition.anyOf(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .flags(EntityFlagsPredicate.Builder.flags()
                                    .setOnFire(true))
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.DIRECT_ATTACKER,
                            EntityPredicate.Builder.entity()
                                .equipment(EntityEquipmentPredicate.Builder.equipment()
                                    .mainhand(ItemPredicate.Builder.item()
                                        .withComponents(DataComponentMatchers.Builder.components()
                                            .partial(DataComponentPredicates.ENCHANTMENTS,
                                                EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(
                                                    registries.lookup(Registries.ENCHANTMENT)
                                                        .flatMap(it ->
                                                            it.get(EnchantmentTags.SMELTS_LOOT)),
                                                    MinMaxBounds.Ints.ANY
                                                ))))
                                            .build())))
                        )
                    ).build())
                )
                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1f, 4f)))
            )
        );
    }
}
