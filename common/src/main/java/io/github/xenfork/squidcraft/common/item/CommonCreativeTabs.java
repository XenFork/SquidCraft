package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.Backend;
import io.github.xenfork.squidcraft.common.CommonIdentifier;

/**
 * @since 0.14.0
 */
public final class CommonCreativeTabs {
    public static final CommonCreativeTab MAIN = new CommonCreativeTab.Builder(new CommonIdentifier("main"))
        .titleTranslationKey("itemGroup.squidcraft.main")
        .icon(CommonItems.SHREDDED_SQUID)
        .displayItems(output -> {
            output.add(VanillaItem.SQUID_SPAWN_EGG);
            output.add(VanillaItem.GLOW_SQUID_SPAWN_EGG);
            output.add(CommonItems.SHREDDED_SQUID);
            output.add(CommonItems.COOKED_SHREDDED_SQUID);
            output.add(CommonItems.GLOW_SHREDDED_SQUID);
            output.add(CommonItems.MAGMA_SHREDDED_SQUID);
            output.add(CommonItems.COOKED_SHREDDED_SQUID_BLOCK);
        })
        .build();

    public static void registerAll(Backend backend) {
        backend.registerCreativeTab(MAIN);
    }
}
