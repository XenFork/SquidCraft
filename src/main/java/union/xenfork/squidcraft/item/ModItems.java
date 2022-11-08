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

package union.xenfork.squidcraft.item;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import union.xenfork.squidcraft.SquidCraft;

/**
 * @author squid233
 * @since 0.13.0
 */
public enum ModItems implements ItemConvertible {
    SQUID_SHRED(new Item(mainGroup().food(meat(1, 0.3f).snack().build()))),
    SQUID_STRIP(new Item(mainGroup().food(meat(1, 0.3f).snack().build()))),
    SQUID_SLICE(new Item(mainGroup().food(meat(2, 0.3f).build()))),
    COOKED_SQUID_SHRED(new Item(mainGroup().food(meat(2, 0.6f).snack().build()))),
    COOKED_SQUID_STRIP(new Item(mainGroup().food(meat(2, 0.6f).snack().build()))),
    COOKED_SQUID_SLICE(new Item(mainGroup().food(meat(4, 0.6f).build()))),
    SQUID_CAKE(new Item(mainGroup().food(meat(6, 0.8f).build())));

    private final Item item;
    private final Identifier id;

    ModItems(Item item) {
        this.item = item;
        id = new Identifier(SquidCraft.NAMESPACE, name().toLowerCase());
    }

    public static void registerAll() {
        for (ModItems v : values()) {
            Registry.register(Registry.ITEM, v.id, v.item);
        }
    }

    private static Item.Settings mainGroup() {
        return new Item.Settings().group(ModItemGroups.MAIN);
    }

    private static FoodComponent.Builder meat(int hunger, float saturationMod) {
        return new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationMod).meat();
    }

    public Item item() {
        return item;
    }

    public Identifier id() {
        return id;
    }

    @Override
    public Item asItem() {
        return item;
    }
}
