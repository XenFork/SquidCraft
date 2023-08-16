/*
 * MIT License
 *
 * Copyright (c) 2023 XenFork Union
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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 0.14.0
 */
public enum ModItems {
    SHREDDED_SQUID(() -> ofSnack(1, 1f)),
    COOKED_SHREDDED_SQUID(() -> ofSnack(2, 1f));

    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(SquidCraft.MOD_ID, Registries.ITEM);
    private final String path;
    private final Supplier<Item> supplier;
    private RegistrySupplier<Item> registrySupplier;

    ModItems(Supplier<Item> supplier, String path) {
        this.path = path;
        this.supplier = supplier;
    }

    ModItems(Supplier<Item> supplier) {
        this.path = name().toLowerCase(Locale.ROOT);
        this.supplier = supplier;
    }

    public static void init() {
        for (ModItems value : values()) {
            value.registrySupplier = REGISTER.register(value.path(), value.supplier);
        }
        REGISTER.register();
    }

    private static Item ofSnack(int nutrition, float saturationMod) {
        return new Item(new Item.Properties().food(new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationMod(saturationMod)
            .fast()
            .meat()
            .build()));
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
