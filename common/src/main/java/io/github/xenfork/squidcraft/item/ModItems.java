/*
 * MIT License
 *
 * Copyright (c) 2023-2024 XenFork Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package io.github.xenfork.squidcraft.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.xenfork.squidcraft.SquidCraft;
import io.github.xenfork.squidcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 0.14.0
 */
public enum ModItems {
    SHREDDED_SQUID(() -> ofSnack(1, 1f)),
    COOKED_SHREDDED_SQUID(() -> ofSnack(2, 1f)),
    GLOW_SHREDDED_SQUID(() -> ofMeat(1, 1f, builder -> builder.effect(new MobEffectInstance(MobEffects.GLOWING, 200, 0), 1f))),
    MAGMA_SHREDDED_SQUID(() -> ofMeat(2, 0.5f, builder -> builder.effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.3f).alwaysEat())),
    COOKED_SHREDDED_SQUID_BLOCK(() -> new BlockItem(ModBlocks.COOKED_SHREDDED_SQUID_BLOCK.get(), new Item.Properties()));

    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(SquidCraft.MOD_ID, Registries.ITEM);
    private final String path;
    private final Supplier<Item> supplier;
    private RegistrySupplier<Item> registrySupplier;

    ModItems(Supplier<Item> supplier) {
        this.path = name().toLowerCase(Locale.ROOT);
        this.supplier = supplier;
    }

    public static void init() {
        for (var value : values()) {
            value.registrySupplier = REGISTER.register(value.path(), value.supplier);
        }
        REGISTER.register();
    }

    private static Item ofMeat(int nutrition, float saturationMod, Consumer<FoodProperties.Builder> consumer) {
        final FoodProperties.Builder builder = new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationMod(saturationMod)
            .meat();
        consumer.accept(builder);
        return new Item(new Item.Properties().food(builder.build()));
    }

    private static Item ofSnack(int nutrition, float saturationMod) {
        return ofMeat(nutrition, saturationMod, FoodProperties.Builder::fast);
    }

    public String path() {
        return path;
    }

    public RegistrySupplier<Item> registrySupplier() {
        return registrySupplier;
    }

    public Item get() {
        return registrySupplier().get();
    }
}
