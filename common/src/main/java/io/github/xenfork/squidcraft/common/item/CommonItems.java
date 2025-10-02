package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonEffect;
import io.github.xenfork.squidcraft.common.CommonEffectInstance;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlocks;

/**
 * @since 0.14.0
 */
public final class CommonItems {
    public static final CommonItem SHREDDED_SQUID = new CommonItem.Builder(new CommonIdentifier("shredded_squid"))
        .foodComponent(new CommonFoodComponent.Builder()
            .nutrition(1)
            .saturationModifier(1)
            .meat()
            .build())
        .consumableComponent(new CommonConsumableComponent.Builder()
            .fast()
            .build())
        .build();
    public static final CommonItem COOKED_SHREDDED_SQUID = new CommonItem.Builder(new CommonIdentifier("cooked_shredded_squid"))
        .foodComponent(new CommonFoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(1)
            .meat()
            .build())
        .consumableComponent(new CommonConsumableComponent.Builder()
            .fast()
            .build())
        .build();
    public static final CommonItem GLOW_SHREDDED_SQUID = new CommonItem.Builder(new CommonIdentifier("glow_shredded_squid"))
        .foodComponent(new CommonFoodComponent.Builder()
            .nutrition(1)
            .saturationModifier(1)
            .meat()
            .build())
        .consumableComponent(new CommonConsumableComponent.Builder()
            .effect(new CommonEffectInstance(CommonEffect.GLOWING, 200, 0), 1)
            .build())
        .build();
    public static final CommonItem MAGMA_SHREDDED_SQUID = new CommonItem.Builder(new CommonIdentifier("magma_shredded_squid"))
        .foodComponent(new CommonFoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.5f)
            .meat()
            .alwaysEat()
            .build())
        .consumableComponent(new CommonConsumableComponent.Builder()
            .effect(new CommonEffectInstance(CommonEffect.FIRE_RESISTANCE, 200, 0), 0.3f)
            .build())
        .build();
    public static final CommonItem COOKED_SHREDDED_SQUID_BLOCK = new CommonItem.Builder(new CommonIdentifier("cooked_shredded_squid_block"))
        .itemBlock(CommonBlocks.COOKED_SHREDDED_SQUID_BLOCK)
        .build();

    public static void registerAll(Backend backend) {
        backend.registerItem(SHREDDED_SQUID);
        backend.registerItem(COOKED_SHREDDED_SQUID);
        backend.registerItem(GLOW_SHREDDED_SQUID);
        backend.registerItem(MAGMA_SHREDDED_SQUID);
        backend.registerItem(COOKED_SHREDDED_SQUID_BLOCK);
    }
}
