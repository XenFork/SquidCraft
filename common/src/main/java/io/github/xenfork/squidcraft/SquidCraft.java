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

package io.github.xenfork.squidcraft;

import dev.architectury.event.events.common.LootEvent;
import io.github.xenfork.squidcraft.block.ModBlocks;
import io.github.xenfork.squidcraft.item.ModItemGroups;
import io.github.xenfork.squidcraft.item.ModItems;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * @author squid233
 * @since 0.14.0
 */
public class SquidCraft {
    public static final String MOD_ID = "squidcraft";

    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModItemGroups.init();
        LootEvent.MODIFY_LOOT_TABLE.register((lootDataManager, id, context, builtin) -> {
            if (builtin) {
                if (EntityType.SQUID.getDefaultLootTable().equals(id)) {
                    addLootItem(context, ModItems.SHREDDED_SQUID.get());
                } else if (EntityType.GLOW_SQUID.getDefaultLootTable().equals(id)) {
                    addLootItem(context, ModItems.GLOWING_SHREDDED_SQUID.get());
                }
            }
        });
    }

    private static void addLootItem(LootEvent.LootTableModificationContext context, Item item) {
        context.addPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 8f)))
                .apply(SmeltItemFunction.smelted().when(() ->
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                            .flags(EntityFlagsPredicate.Builder.flags()
                                .setOnFire(true)
                                .build())
                    ).build())
                )
                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1f, 4f)))
            )
        );
    }
}
