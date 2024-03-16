/*
 * MIT License
 *
 * Copyright (c) 2024 XenFork Union
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

package io.github.xenfork.squidcraft.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.xenfork.squidcraft.SquidCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 0.14.0
 */
public enum ModBlocks {
    COOKED_SHREDDED_SQUID_BLOCK(() -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).instabreak().sound(SoundType.SLIME_BLOCK)));

    private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(SquidCraft.MOD_ID, Registries.BLOCK);
    private final String path;
    private final Supplier<Block> supplier;
    private RegistrySupplier<Block> registrySupplier;

    ModBlocks(Supplier<Block> supplier) {
        this.path = name().toLowerCase(Locale.ROOT);
        this.supplier = supplier;
    }

    public static void init() {
        for (var value : values()) {
            value.registrySupplier = REGISTER.register(value.path(), value.supplier);
        }
        REGISTER.register();
    }

    public String path() {
        return path;
    }

    public RegistrySupplier<Block> registrySupplier() {
        return registrySupplier;
    }

    public Block get() {
        return registrySupplier().get();
    }
}
