package io.github.xenfork.squidcraft.neoforge;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import java.util.List;

/**
 * @since 0.14.0
 */
public final class NeoForgeBackend implements Backend {
    private final IEventBus modEventBus;

    public NeoForgeBackend(IEventBus modEventBus) {
        this.modEventBus = modEventBus;
    }

    @Override
    public void registerBlock(CommonBlock block) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        properties.mapColor(switch (block.mapColor()) {
            case TERRACOTTA_WHITE -> MapColor.TERRACOTTA_WHITE;
        });
        if (block.instantBreak()) {
            properties.instabreak();
        }
        properties.sound(switch (block.soundType()) {
            case SLIME_BLOCK -> SoundType.SLIME_BLOCK;
        });

        SquidCraftNeoForge.BLOCKS.registerSimpleBlock(block.identifier().path(), properties);
    }

    @Override
    public void registerItem(CommonItem item) {
        SquidCraftNeoForge.ITEMS.registerItem(item.identifier().path(), properties -> {
            CommonFoodComponent commonFoodComponent = item.foodComponent();
            CommonConsumableComponent commonConsumableComponent = item.consumableComponent();
            CommonBlock commonBlock = item.itemBlock();

            if (commonFoodComponent != null) {
                FoodProperties.Builder builder = new FoodProperties.Builder()
                    .nutrition(commonFoodComponent.nutrition())
                    .saturationModifier(commonFoodComponent.saturationModifier());
                if (commonFoodComponent.alwaysEat()) {
                    builder.alwaysEdible();
                }
                properties.food(builder.build());
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
                properties.component(DataComponents.CONSUMABLE, builder.build());
            }

            Item item1;
            if (commonBlock != null) {
                properties.useBlockDescriptionPrefix();
                item1 = new BlockItem(RegUtil.block(commonBlock), properties);
            } else {
                item1 = new Item(properties);
            }
            return item1;
        });
    }

    @Override
    public void registerCreativeTab(CommonCreativeTab creativeTab) {
        SquidCraftNeoForge.CREATIVE_TABS.register(creativeTab.identifier().path(),
            () -> CreativeModeTab.builder()
                .title(Component.translatable(creativeTab.titleTranslationKey()))
                .icon(() -> new ItemStack(RegUtil.item(creativeTab.icon())))
                .displayItems((itemDisplayParameters, output) -> {
                    for (ICommonItem displayItem : creativeTab.displayItems()) {
                        output.accept(RegUtil.item(displayItem));
                    }
                })
                .build());
    }

    @Override
    public void registerLootTables() {
        NeoForge.EVENT_BUS.addListener(this::onLootTableLoad);
    }

    private void onLootTableLoad(LootTableLoadEvent event) {
        ResourceKey<LootTable> key = event.getKey();
        LootTable lootTable = event.getTable();
        HolderLookup.Provider registries = event.getRegistries();
        if (EntityType.SQUID.getDefaultLootTable().isPresent() && EntityType.SQUID.getDefaultLootTable().get().equals(key)) {
            addLootItem(lootTable, registries, RegUtil.item(CommonItems.SHREDDED_SQUID));
        } else if (EntityType.GLOW_SQUID.getDefaultLootTable().isPresent() && EntityType.GLOW_SQUID.getDefaultLootTable().get().equals(key)) {
            addLootItem(lootTable, registries, RegUtil.item(CommonItems.GLOW_SHREDDED_SQUID));
        }
    }

    private static void addLootItem(LootTable lootTable, HolderLookup.Provider registries, Item item) {
        lootTable.addPool(LootPool.lootPool()
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
            .build()
        );
    }
}
