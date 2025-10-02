package io.github.xenfork.squidcraft.common.block;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.CommonSoundType;

/**
 * @since 0.14.0
 */
public final class CommonBlock implements CommonIdentifiable {
    private final CommonIdentifier identifier;
    private final CommonMapColor mapColor;
    private final boolean instantBreak;
    private final CommonSoundType soundType;

    private CommonBlock(Builder builder) {
        this.identifier = builder.identifier;
        this.mapColor = builder.mapColor;
        this.instantBreak = builder.instantBreak;
        this.soundType = builder.soundType;
    }

    public static final class Builder {
        private final CommonIdentifier identifier;
        private CommonMapColor mapColor;
        private boolean instantBreak = false;
        private CommonSoundType soundType;

        public Builder(CommonIdentifier identifier) {
            this.identifier = identifier;
        }

        public Builder mapColor(CommonMapColor mapColor) {
            this.mapColor = mapColor;
            return this;
        }

        public Builder instantBreak() {
            this.instantBreak = true;
            return this;
        }

        public Builder soundType(CommonSoundType soundType) {
            this.soundType = soundType;
            return this;
        }

        public CommonBlock build() {
            return new CommonBlock(this);
        }
    }

    @Override
    public CommonIdentifier identifier() {
        return identifier;
    }

    public CommonMapColor mapColor() {
        return mapColor;
    }

    public boolean instantBreak() {
        return instantBreak;
    }

    public CommonSoundType soundType() {
        return soundType;
    }
}
