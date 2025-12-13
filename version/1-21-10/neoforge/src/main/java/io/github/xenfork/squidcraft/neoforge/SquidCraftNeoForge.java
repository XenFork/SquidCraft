package io.github.xenfork.squidcraft.neoforge;

import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @since 0.14.0
 */
@Mod(SquidCraftCommon.MOD_ID)
public class SquidCraftNeoForge {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SquidCraftCommon.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SquidCraftCommon.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, SquidCraftCommon.MOD_ID);

    public SquidCraftNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        SquidCraftCommon.initialize(new NeoForgeBackend(modEventBus));
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        modEventBus.register(SquidCraftNeoForgeDataGen.class);
    }
}
