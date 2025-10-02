package io.github.xenfork.squidcraft.common;

/**
 * @since 0.14.0
 */
public record CommonIdentifier(String namespace, String path) implements CommonIdentifiable {
    public CommonIdentifier(String path) {
        this(SquidCraftCommon.MOD_ID, path);
    }

    @Override
    public CommonIdentifier identifier() {
        return this;
    }
}
