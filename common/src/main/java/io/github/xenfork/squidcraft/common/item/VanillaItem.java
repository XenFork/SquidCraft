package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.CommonIdentifier;

/**
 * @since 0.14.0
 */
public enum VanillaItem implements ICommonItem {
    GLOW_SQUID_SPAWN_EGG(new CommonIdentifier("minecraft", "glow_squid_spawn_egg")),
    MAGMA_CREAM(new CommonIdentifier("minecraft", "magma_cream")),
    SQUID_SPAWN_EGG(new CommonIdentifier("minecraft", "squid_spawn_egg")),
    ;

    private final CommonIdentifier identifier;

    VanillaItem(CommonIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public CommonIdentifier identifier() {
        return identifier;
    }
}
