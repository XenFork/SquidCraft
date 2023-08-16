package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraft;
import net.fabricmc.api.ModInitializer;

public class SquidCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SquidCraft.init();
    }
}
