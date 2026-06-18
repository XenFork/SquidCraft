package io.github.xenfork.squidcraft;

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
    public static final Item SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(SquidCraftItemIds.SHREDDED_SQUID)
        .food(new FoodProperties(1, 1f, false),
            Consumable.builder()
                .consumeSeconds(0.8f)
                .build()));
    public static final Item COOKED_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(SquidCraftItemIds.COOKED_SHREDDED_SQUID)
        .food(new FoodProperties(2, 1f, false),
            Consumable.builder()
                .consumeSeconds(0.8f)
                .build()));
    public static final Item GLOW_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(SquidCraftItemIds.GLOW_SHREDDED_SQUID)
        .food(new FoodProperties(1, 1f, false),
            Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0)))
                .build()));
    public static final Item MAGMA_SHREDDED_SQUID = new Item(new Item.Properties()
        .setId(SquidCraftItemIds.MAGMA_SHREDDED_SQUID)
        .food(new FoodProperties(2, 0.5f, true),
            Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.3f))
                .build()));
    public static final BlockItem COOKED_SHREDDED_SQUID_BLOCK = new BlockItem(SquidCraftBlocks.COOKED_SHREDDED_SQUID_BLOCK, new Item.Properties()
        .setId(SquidCraftBlockItemIds.COOKED_SHREDDED_SQUID_BLOCK.item())
        .useBlockDescriptionPrefix());

    private SquidCraftItems() {
    }

    @FunctionalInterface
    public interface Registry {
        void register(ResourceKey<Item> id, Item item);
    }

    public static void registerAll(Registry registry) {
        registry.register(SquidCraftItemIds.SHREDDED_SQUID, SHREDDED_SQUID);
        registry.register(SquidCraftItemIds.COOKED_SHREDDED_SQUID, COOKED_SHREDDED_SQUID);
        registry.register(SquidCraftItemIds.GLOW_SHREDDED_SQUID, GLOW_SHREDDED_SQUID);
        registry.register(SquidCraftItemIds.MAGMA_SHREDDED_SQUID, MAGMA_SHREDDED_SQUID);
        registry.register(SquidCraftBlockItemIds.COOKED_SHREDDED_SQUID_BLOCK.item(), COOKED_SHREDDED_SQUID_BLOCK);
    }
}
