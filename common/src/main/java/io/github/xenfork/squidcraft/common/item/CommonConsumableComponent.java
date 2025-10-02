package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.CommonEffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 0.14.0
 */
public final class CommonConsumableComponent {
    private final boolean fast;
    private final List<Map.Entry<CommonEffectInstance, Float>> effects;

    private CommonConsumableComponent(Builder builder) {
        this.fast = builder.fast;
        this.effects = List.copyOf(builder.effects);
    }

    public static final class Builder {
        private boolean fast = false;
        private final List<Map.Entry<CommonEffectInstance, Float>> effects = new ArrayList<>();

        public Builder fast() {
            this.fast = true;
            return this;
        }

        public Builder effect(CommonEffectInstance effect, float chance) {
            this.effects.add(Map.entry(effect, chance));
            return this;
        }

        public CommonConsumableComponent build() {
            return new CommonConsumableComponent(this);
        }
    }

    public boolean fast() {
        return fast;
    }

    public List<Map.Entry<CommonEffectInstance, Float>> effects() {
        return effects;
    }
}
