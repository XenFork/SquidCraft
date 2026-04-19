package io.github.xenfork.squidcraft;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// @since 26.1.0
public final class SquidCraftCommon {
    public static final String MOD_ID = "squidcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private SquidCraftCommon() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
