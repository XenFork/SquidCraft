package io.github.xenfork.squidcraft.common;

import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.CommonCreativeTab;
import io.github.xenfork.squidcraft.common.item.CommonItem;

/**
 * @since 0.14.0
 */
public interface Backend {
    void registerBlock(CommonBlock block);

    void registerItem(CommonItem item);

    void registerCreativeTab(CommonCreativeTab creativeTab);

    void registerLootTables();
}
