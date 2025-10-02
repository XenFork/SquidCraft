package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.CommonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class CommonModelProvider {
    private final List<CommonIdentifier> blockCubeAll = new ArrayList<>();
    private final List<CommonIdentifier> itemGenerated = new ArrayList<>();

    public void registerCubeAll(CommonBlock block) {
        blockCubeAll.add(block.identifier());
    }

    public void registerGenerated(CommonItem item) {
        itemGenerated.add(item.identifier());
    }

    public List<CommonIdentifier> blockCubeAll() {
        return blockCubeAll;
    }

    public List<CommonIdentifier> itemGenerated() {
        return itemGenerated;
    }
}
