package io.github.xenfork.squidcraft.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.xenfork.squidcraft.SquidCraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SquidCraft.MOD_ID)
public class SquidCraftForge {
    public SquidCraftForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SquidCraft.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SquidCraft.init();
    }
}
