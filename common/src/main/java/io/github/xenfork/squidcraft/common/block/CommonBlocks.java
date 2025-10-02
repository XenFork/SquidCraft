package io.github.xenfork.squidcraft.common.block;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.CommonSoundType;

/**
 * @since 0.14.0
 */
public final class CommonBlocks {
    public static final CommonBlock COOKED_SHREDDED_SQUID_BLOCK = new CommonBlock.Builder(new CommonIdentifier("cooked_shredded_squid_block"))
        .mapColor(CommonMapColor.TERRACOTTA_WHITE)
        .instantBreak()
        .soundType(CommonSoundType.SLIME_BLOCK)
        .build();

    public static void registerAll(Backend backend) {
        backend.registerBlock(COOKED_SHREDDED_SQUID_BLOCK);
    }
}
