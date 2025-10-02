package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class CommonBlockLootTableProvider {
    private final List<CommonIdentifier> drops = new ArrayList<>();

    public void add(CommonBlock block) {
        drops.add(block.identifier());
    }

    public List<CommonIdentifier> drops() {
        return drops;
    }
}
