package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import net.fabricmc.api.ModInitializer;

/**
 * @since 0.15.0
 */
public class SquidCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SquidCraftCommon.initialize(new FabricBackend());
    }
}
