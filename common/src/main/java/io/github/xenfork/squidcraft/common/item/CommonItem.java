package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;

/**
 * @since 0.14.0
 */
public final class CommonItem implements ICommonItem {
    private final CommonIdentifier identifier;
    private final CommonFoodComponent foodComponent;
    private final CommonConsumableComponent consumableComponent;
    private final CommonBlock itemBlock;

    private CommonItem(Builder builder) {
        this.identifier = builder.identifier;
        this.foodComponent = builder.foodComponent;
        this.consumableComponent = builder.consumableComponent;
        this.itemBlock = builder.itemBlock;
    }

    public static final class Builder {
        private final CommonIdentifier identifier;
        private CommonFoodComponent foodComponent = null;
        private CommonConsumableComponent consumableComponent = null;
        private CommonBlock itemBlock = null;

        public Builder(CommonIdentifier identifier) {
            this.identifier = identifier;
        }

        public Builder foodComponent(CommonFoodComponent foodComponent) {
            this.foodComponent = foodComponent;
            return this;
        }

        public Builder consumableComponent(CommonConsumableComponent consumableComponent) {
            this.consumableComponent = consumableComponent;
            return this;
        }

        public Builder itemBlock(CommonBlock itemBlock) {
            this.itemBlock = itemBlock;
            return this;
        }

        public CommonItem build() {
            return new CommonItem(this);
        }
    }

    @Override
    public CommonIdentifier identifier() {
        return identifier;
    }

    public CommonFoodComponent foodComponent() {
        return foodComponent;
    }

    public CommonConsumableComponent consumableComponent() {
        return consumableComponent;
    }

    public CommonBlock itemBlock() {
        return itemBlock;
    }
}
