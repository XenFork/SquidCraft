package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @since 0.14.0
 */
public final class FabricBackend implements Backend {
    @Override
    public void registerBlock(CommonBlock block) {
        CommonIdentifier identifier = block.identifier();

        AbstractBlock.Settings settings = AbstractBlock.Settings.create();
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
            new Identifier(identifier.namespace(), identifier.path()),
            new Block(settings));
    }

    @Override
    public void registerItem(CommonItem item) {
        CommonIdentifier identifier = item.identifier();
        CommonFoodComponent commonFoodComponent = item.foodComponent();
        CommonConsumableComponent consumableComponent = item.consumableComponent();
        CommonBlock commonBlock = item.itemBlock();

        Item.Settings settings = new Item.Settings();
        if (commonFoodComponent != null) {
            FoodComponent.Builder builder = new FoodComponent.Builder()
                .hunger(commonFoodComponent.nutrition())
                .saturationModifier(commonFoodComponent.saturationModifier());
            if (commonFoodComponent.meat()) {
                builder.meat();
            }
            if (commonFoodComponent.alwaysEat()) {
                builder.alwaysEdible();
            }
            if (consumableComponent != null) {
                if (consumableComponent.fast()) {
                    builder.snack();
                }
                for (var entry : consumableComponent.effects()) {
                    CommonEffectInstance instance = entry.getKey();
                    float chance = entry.getValue();
                    builder.statusEffect(new StatusEffectInstance(switch (instance.effect()) {
                        case FIRE_RESISTANCE -> StatusEffects.FIRE_RESISTANCE;
                        case GLOWING -> StatusEffects.GLOWING;
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
        Registry.register(Registries.ITEM,
            new Identifier(identifier.namespace(), identifier.path()),
            item1);
    }

    @Override
    public void registerCreativeTab(CommonCreativeTab creativeTab) {
        CommonIdentifier identifier = creativeTab.identifier();
        Registry.register(Registries.ITEM_GROUP,
            new Identifier(identifier.namespace(), identifier.path()),
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
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (source.isBuiltin()) {
                if (EntityType.SQUID.getLootTableId().equals(id)) {
                    addLootItem(builder, RegUtil.item(CommonItems.SHREDDED_SQUID));
                } else if (EntityType.GLOW_SQUID.getLootTableId().equals(id)) {
                    addLootItem(builder, RegUtil.item(CommonItems.GLOW_SHREDDED_SQUID));
                }
            }
        });
    }

    private static void addLootItem(LootTable.Builder builder, Item item) {
        builder.pool(LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1f))
            .with(ItemEntry.builder(item)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 8f)))
                .apply(FurnaceSmeltLootFunction.builder().conditionally(() ->
                    EntityPropertiesLootCondition.builder(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.create()
                            .flags(EntityFlagsPredicate.Builder.create()
                                .onFire(true)
                                .build())
                    ).build())
                )
                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(1f, 4f)))
            )
        );
    }
}
