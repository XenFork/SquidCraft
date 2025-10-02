package io.github.xenfork.squidcraft.common.item;

/**
 * @since 0.14.0
 */
public final class CommonFoodComponent {
    private final int nutrition;
    private final float saturationModifier;
    private final boolean meat;
    private final boolean alwaysEat;

    private CommonFoodComponent(Builder builder) {
        this.nutrition = builder.nutrition;
        this.saturationModifier = builder.saturationModifier;
        this.meat = builder.meat;
        this.alwaysEat = builder.alwaysEat;
    }

    public static final class Builder {
        private int nutrition;
        private float saturationModifier;
        private boolean meat = false;
        private boolean alwaysEat = false;

        public Builder nutrition(int nutrition) {
            this.nutrition = nutrition;
            return this;
        }

        public Builder saturationModifier(float saturationModifier) {
            this.saturationModifier = saturationModifier;
            return this;
        }

        public Builder meat() {
            this.meat = true;
            return this;
        }

        public Builder alwaysEat() {
            this.alwaysEat = true;
            return this;
        }

        public CommonFoodComponent build() {
            return new CommonFoodComponent(this);
        }
    }

    public int nutrition() {
        return nutrition;
    }

    public float saturationModifier() {
        return saturationModifier;
    }

    public boolean meat() {
        return meat;
    }

    public boolean alwaysEat() {
        return alwaysEat;
    }
}
