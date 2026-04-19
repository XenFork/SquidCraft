package io.github.xenfork.squidcraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/// @since 26.1.0
public final class SquidCraftCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> MAIN_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, SquidCraftCommon.id("main"));
    public static final CommonCreativeModeTab MAIN = new CommonCreativeModeTab.Builder()
        .title(Component.translatable("itemGroup.squidcraft.main"))
        .icon(() -> new ItemStack(SquidCraftItems.SHREDDED_SQUID))
        .displayItems((_, output) -> {
            output.accept(Items.SQUID_SPAWN_EGG);
            output.accept(Items.GLOW_SQUID_SPAWN_EGG);
            output.accept(SquidCraftItems.SHREDDED_SQUID);
            output.accept(SquidCraftItems.COOKED_SHREDDED_SQUID);
            output.accept(SquidCraftItems.GLOW_SHREDDED_SQUID);
            output.accept(SquidCraftItems.MAGMA_SHREDDED_SQUID);
            output.accept(SquidCraftItems.COOKED_SHREDDED_SQUID_BLOCK);
        })
        .build();

    private SquidCraftCreativeModeTabs() {
    }
}
