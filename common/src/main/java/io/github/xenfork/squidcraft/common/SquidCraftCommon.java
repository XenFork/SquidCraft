package io.github.xenfork.squidcraft.common;

import io.github.xenfork.squidcraft.common.block.CommonBlocks;
import io.github.xenfork.squidcraft.common.item.CommonCreativeTabs;
import io.github.xenfork.squidcraft.common.item.CommonItems;

/**
 * @since 0.14.0
 */
public final class SquidCraftCommon {
    public static final String MOD_ID = "squidcraft";

    public static void initialize(Backend backend) {
        CommonBlocks.registerAll(backend);
        CommonItems.registerAll(backend);
        CommonCreativeTabs.registerAll(backend);
        backend.registerLootTables();
    }
}
