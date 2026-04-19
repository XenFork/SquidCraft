package io.github.xenfork.squidcraft.neoforge;

import io.github.xenfork.squidcraft.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/// @since 0.14.0
@Mod(SquidCraftCommon.MOD_ID)
@EventBusSubscriber(modid = SquidCraftCommon.MOD_ID)
public class SquidCraftNeoForge {
    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(Registries.BLOCK, registry -> SquidCraftBlocks.registerAll(registry::register));
        event.register(Registries.ITEM, registry -> SquidCraftItems.registerAll(registry::register));
        event.register(Registries.CREATIVE_MODE_TAB, registry ->
            registry.register(SquidCraftCreativeModeTabs.MAIN_KEY, creativeModeTab(SquidCraftCreativeModeTabs.MAIN)));
    }

    private static CreativeModeTab creativeModeTab(CommonCreativeModeTab commonCreativeModeTab) {
        return CreativeModeTab.builder()
            .title(commonCreativeModeTab.title)
            .icon(commonCreativeModeTab.icon)
            .displayItems((parameters, output) ->
                commonCreativeModeTab.displayItems.accept(parameters, output::accept))
            .build();
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            SquidCraftCreativeModeTabs.addFoods(event::accept);
        }
    }
}
