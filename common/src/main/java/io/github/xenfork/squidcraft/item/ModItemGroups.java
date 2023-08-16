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

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.xenfork.squidcraft.SquidCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * @author squid233
 * @since 0.14.0
 */
public final class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(SquidCraft.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> MAIN = REGISTER.register("main",
        () -> CreativeTabRegistry.create(builder -> builder
            .title(Component.translatable("itemGroup.squidcraft.main"))
            .icon(() -> new ItemStack(ModItems.SHREDDED_SQUID.get()))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(Items.SQUID_SPAWN_EGG);
                output.accept(ModItems.SHREDDED_SQUID.get());
                output.accept(ModItems.COOKED_SHREDDED_SQUID.get());
            })
        ));

    public static void init() {
        REGISTER.register();
    }
}
