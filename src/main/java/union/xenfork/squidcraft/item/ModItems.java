/*
 * MIT License
 *
 * Copyright (c) 2022-2023 XenFork Union
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

package union.xenfork.squidcraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import union.xenfork.squidcraft.SquidCraft;
import union.xenfork.squidcraft.block.ModBlocks;

/**
 * @author squid233
 * @since 0.13.0
 */
public final class ModItems {
    public static final Item
        SQUID_SHRED = register("squid_shred", settings().food(meat(1, 0.3f, true))),
        SQUID_STRIP = register("squid_strip", settings().food(meat(1, 0.3f, true))),
        SQUID_SLICE = register("squid_slice", settings().food(meat(2, 0.3f))),
        COOKED_SQUID_SHRED = register("cooked_squid_shred", settings().food(meat(2, 0.6f, true))),
        COOKED_SQUID_STRIP = register("cooked_squid_strip", settings().food(meat(2, 0.6f, true))),
        COOKED_SQUID_SLICE = register("cooked_squid_slice", settings().food(meat(3, 0.6f))),
        SQUID_CAKE = register("squid_cake", settings().food(meat(8, 0.8f))),
        SQUID_BLOCK = register("squid_block", ModBlocks.SQUID_BLOCK, settings().food(meat(10, 1.0f))),
        DICED_CARROT = register("diced_carrot", settings().food(food(1, 0.2f, true))),
        KNIFE = register("knife", new KnifeItem(ToolMaterials.IRON, 0, 0.0f, settings()));

    public static void registerAll() {
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SquidCraft.NAMESPACE, name), item);
    }

    private static Item register(String name, Item.Settings settings) {
        return register(name, new Item(settings));
    }

    private static Item register(String name, Block block, Item.Settings settings) {
        return register(name, new BlockItem(block, settings));
    }

    private static Item.Settings settings() {
        return new Item.Settings();
    }

    private static FoodComponent.Builder foodBuilder(int hunger, float saturationMod) {
        return new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationMod);
    }

    private static FoodComponent food(int hunger, float saturationMod, boolean snack) {
        var builder = foodBuilder(hunger, saturationMod);
        if (snack) builder.snack();
        return builder.build();
    }

    private static FoodComponent meat(int hunger, float saturationMod, boolean snack) {
        var builder = foodBuilder(hunger, saturationMod).meat();
        if (snack) builder.snack();
        return builder.build();
    }

    private static FoodComponent meat(int hunger, float saturationMod) {
        return meat(hunger, saturationMod, false);
    }
}
