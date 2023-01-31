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

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import union.xenfork.squidcraft.SquidCraft;

import java.util.List;

import static union.xenfork.squidcraft.item.ModItems.*;

/**
 * @author squid233
 * @since 0.13.0
 */
public final class ModItemGroups {
    public static final ItemGroup MAIN = FabricItemGroupBuilder.create(new Identifier(SquidCraft.NAMESPACE, "main"))
        .icon(() -> new ItemStack(ModItems.COOKED_SQUID_SLICE))
        .appendItems(stacks -> addItems(stacks,
            Items.SQUID_SPAWN_EGG,
            SQUID_SHRED,
            SQUID_STRIP,
            SQUID_SLICE,
            COOKED_SQUID_SHRED,
            COOKED_SQUID_STRIP,
            COOKED_SQUID_SLICE,
            SQUID_CAKE,
            SQUID_BLOCK,
            DICED_CARROT,
            KNIFE
        ))
        .build();

    public static void registerAll() {
    }

    private static void addItems(List<ItemStack> stacks, ItemConvertible... items) {
        for (ItemConvertible item : items) {
            stacks.add(new ItemStack(item));
        }
    }
}
