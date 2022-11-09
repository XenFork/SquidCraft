/*
 * MIT License
 *
 * Copyright (c) 2022 XenFork Union
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
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package union.xenfork.squidcraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import union.xenfork.squidcraft.block.ModBlocks;
import union.xenfork.squidcraft.item.ModItems;

/**
 * @author squid233
 * @since 0.13.0
 */
public final class SquidCraft implements ModInitializer {
    public static final String NAMESPACE = "squidcraft";
    public static final Identifier SQUID_LOOT_TABLE_ID = EntityType.SQUID.getLootTableId();

    @Override
    public void onInitialize() {
        ModBlocks.registerAll();
        ModItems.registerAll();

        // Add items to squid
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && SQUID_LOOT_TABLE_ID.equals(id)) {
                tableBuilder.pool(LootPool.builder()
                    .with(ItemEntry.builder(ModItems.SQUID_SHRED).apply(setCountFun(8, 10)).apply(fireSmelt()).apply(lootingEnchantFun()))
                    .with(ItemEntry.builder(ModItems.SQUID_STRIP).apply(setCountFun(4, 5)).apply(fireSmelt()).apply(lootingEnchantFun()))
                    .with(ItemEntry.builder(ModItems.SQUID_SLICE).apply(setCountFun(1, 3)).apply(fireSmelt()).apply(lootingEnchantFun()))
                    .build());
            }
        });
    }

    private static ConditionalLootFunction.Builder<?> setCountFun(float min, float max) {
        return SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max));
    }

    private static ConditionalLootFunction.Builder<?> lootingEnchantFun() {
        return LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f));
    }

    private static ConditionalLootFunction.Builder<?> fireSmelt() {
        return FurnaceSmeltLootFunction.builder()
            .conditionally(EntityPropertiesLootCondition.builder(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.create()
                    .flags(EntityFlagsPredicate.Builder.create()
                        .onFire(true)
                        .build())
            ));
    }
}
