package io.github.xenfork.squidcraft;

import net.minecraft.references.BlockItemId;

/// @since 26.2.0
public final class SquidCraftBlockItemIds {
    public static final BlockItemId COOKED_SHREDDED_SQUID_BLOCK = create("cooked_shredded_squid_block");

    private SquidCraftBlockItemIds() {
    }

    private static BlockItemId create(String name) {
        return BlockItemId.create(SquidCraftCommon.id(name), SquidCraftCommon.id(name));
    }
}
