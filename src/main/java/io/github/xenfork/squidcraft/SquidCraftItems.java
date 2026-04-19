package io.github.xenfork.squidcraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

/// @since 26.1.0
public final class SquidCraftItems {
    public static final Identifier SHREDDED_SQUID_ID = SquidCraftCommon.id("shredded_squid");
    public static final Identifier COOKED_SHREDDED_SQUID_ID = SquidCraftCommon.id("cooked_shredded_squid");
    public static final Identifier GLOW_SHREDDED_SQUID_ID = SquidCraftCommon.id("glow_shredded_squid");
    public static final Identifier MAGMA_SHREDDED_SQUID_ID = SquidCraftCommon.id("magma_shredded_squid");
    public static final Item SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, SHREDDED_SQUID_ID))
        .food(new FoodProperties(1, 1f, false),
            Consumable.builder()
                .consumeSeconds(0.8f)
                .build()));
    public static final Item COOKED_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, COOKED_SHREDDED_SQUID_ID))
        .food(new FoodProperties(2, 1f, false),
            Consumable.builder()
                .consumeSeconds(0.8f)
                .build()));
    public static final Item GLOW_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, GLOW_SHREDDED_SQUID_ID))
        .food(new FoodProperties(1, 1f, false),
            Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0)))
                .build()));
    public static final Item MAGMA_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, MAGMA_SHREDDED_SQUID_ID))
        .food(new FoodProperties(2, 0.5f, true),
            Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.3f))
                .build()));
    public static final BlockItem COOKED_SHREDDED_SQUID_BLOCK = new BlockItem(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK, new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK_ID))
        .useBlockDescriptionPrefix());

    private SquidCraftItems() {
    }

    @FunctionalInterface
    public interface Registry {
        void register(Identifier id, Item item);
    }

    public static void registerAll(Registry registry) {
        registry.register(SHREDDED_SQUID_ID, SHREDDED_SQUID);
        registry.register(COOKED_SHREDDED_SQUID_ID, COOKED_SHREDDED_SQUID);
        registry.register(GLOW_SHREDDED_SQUID_ID, GLOW_SHREDDED_SQUID);
        registry.register(MAGMA_SHREDDED_SQUID_ID, MAGMA_SHREDDED_SQUID);
        registry.register(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK_ID, COOKED_SHREDDED_SQUID_BLOCK);
    }
}
