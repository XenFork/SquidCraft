package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.AnyOfLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentPredicateTypes;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * @since 0.14.0
 */
public final class FabricBackend implements Backend {
    @Override
    public void registerBlock(CommonBlock block) {
        CommonIdentifier identifier = block.identifier();
        Identifier id = Identifier.of(identifier.namespace(), identifier.path());

        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, id));
        settings.mapColor(switch (block.mapColor()) {
            case TERRACOTTA_WHITE -> MapColor.TERRACOTTA_WHITE;
        });
        if (block.instantBreak()) {
            settings.breakInstantly();
        }
        settings.sounds(switch (block.soundType()) {
            case SLIME_BLOCK -> BlockSoundGroup.SLIME;
        });

        Registry.register(Registries.BLOCK,
            id,
            new Block(settings));
    }

    @Override
    public void registerItem(CommonItem item) {
        CommonIdentifier identifier = item.identifier();
        Identifier id = Identifier.of(identifier.namespace(), identifier.path());
        CommonFoodComponent commonFoodComponent = item.foodComponent();
        CommonConsumableComponent commonConsumableComponent = item.consumableComponent();
        CommonBlock commonBlock = item.itemBlock();

        Item.Settings settings = new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, id));
        if (commonFoodComponent != null) {
            FoodComponent.Builder builder = new FoodComponent.Builder()
                .nutrition(commonFoodComponent.nutrition())
                .saturationModifier(commonFoodComponent.saturationModifier());
            if (commonFoodComponent.alwaysEat()) {
                builder.alwaysEdible();
            }
            settings.food(builder.build());
        }
        if (commonConsumableComponent != null) {
            ConsumableComponent.Builder builder = ConsumableComponent.builder();
            if (commonConsumableComponent.fast()) {
                builder.consumeSeconds(0.8f);
            }
            for (var entry : commonConsumableComponent.effects()) {
                CommonEffectInstance instance = entry.getKey();
                float chance = entry.getValue();
                builder.consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(switch (instance.effect()) {
                    case FIRE_RESISTANCE -> StatusEffects.FIRE_RESISTANCE;
                    case GLOWING -> StatusEffects.GLOWING;
                }, instance.durationTicks(), instance.amplifier()), chance));
            }
            settings.component(DataComponentTypes.CONSUMABLE, builder.build());
        }

        Item item1;
        if (commonBlock != null) {
            settings.useBlockPrefixedTranslationKey();
            item1 = new BlockItem(RegUtil.block(commonBlock), settings);
        } else {
            item1 = new Item(settings);
        }
        Registry.register(Registries.ITEM,
            id,
            item1);
    }

    @Override
    public void registerCreativeTab(CommonCreativeTab creativeTab) {
        CommonIdentifier identifier = creativeTab.identifier();
        Registry.register(Registries.ITEM_GROUP,
            Identifier.of(identifier.namespace(), identifier.path()),
            FabricItemGroup.builder()
                .displayName(Text.translatable(creativeTab.titleTranslationKey()))
                .icon(() -> new ItemStack(RegUtil.item(creativeTab.icon())))
                .entries((displayContext, entries) -> {
                    for (ICommonItem displayItem : creativeTab.displayItems()) {
                        entries.add(RegUtil.item(displayItem));
                    }
                })
                .build());
    }

    @Override
    public void registerLootTables() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (source.isBuiltin()) {
                if (EntityType.SQUID.getLootTableKey().isPresent() && EntityType.SQUID.getLootTableKey().get().equals(key)) {
                    addLootItem(builder, registries, RegUtil.item(CommonItems.SHREDDED_SQUID));
                } else if (EntityType.GLOW_SQUID.getLootTableKey().isPresent() && EntityType.GLOW_SQUID.getLootTableKey().get().equals(key)) {
                    addLootItem(builder, registries, RegUtil.item(CommonItems.GLOW_SHREDDED_SQUID));
                }
            }
        });
    }

    private static void addLootItem(LootTable.Builder builder, RegistryWrapper.WrapperLookup registries, Item item) {
        builder.pool(LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1f))
            .with(ItemEntry.builder(item)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 8f)))
                .apply(FurnaceSmeltLootFunction.builder().conditionally(() ->
                    AnyOfLootCondition.builder(
                        EntityPropertiesLootCondition.builder(
                            LootContext.EntityReference.THIS,
                            EntityPredicate.Builder.create()
                                .flags(EntityFlagsPredicate.Builder.create()
                                    .onFire(true))
                        ),
                        EntityPropertiesLootCondition.builder(
                            LootContext.EntityReference.DIRECT_ATTACKER,
                            EntityPredicate.Builder.create()
                                .equipment(EntityEquipmentPredicate.Builder.create()
                                    .mainhand(ItemPredicate.Builder.create()
                                        .components(ComponentsPredicate.Builder.create()
                                            .partial(ComponentPredicateTypes.ENCHANTMENTS,
                                                EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(
                                                    registries.getOptional(RegistryKeys.ENCHANTMENT)
                                                        .flatMap(it ->
                                                            it.getOptional(EnchantmentTags.SMELTS_LOOT)),
                                                    NumberRange.IntRange.ANY
                                                ))))
                                            .build())))
                        )
                    ).build())
                )
                .apply(EnchantedCountIncreaseLootFunction.builder(registries, UniformLootNumberProvider.create(1f, 4f)))
            )
        );
    }
}
