package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.CommonIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @since 0.14.0
 */
public final class CommonCreativeTab {
    private final CommonIdentifier identifier;
    private final String titleTranslationKey;
    private final CommonItem icon;
    private final List<ICommonItem> displayItems;

    private CommonCreativeTab(Builder builder) {
        this.identifier = builder.identifier;
        this.titleTranslationKey = builder.titleTranslationKey;
        this.icon = builder.icon;
        this.displayItems = List.copyOf(builder.displayItems);
    }

    public static final class Builder {
        private final CommonIdentifier identifier;
        private String titleTranslationKey;
        private CommonItem icon;
        private final List<ICommonItem> displayItems = new ArrayList<>();

        public Builder(CommonIdentifier identifier) {
            this.identifier = identifier;
        }

        public Builder titleTranslationKey(String titleTranslationKey) {
            this.titleTranslationKey = titleTranslationKey;
            return this;
        }

        public Builder icon(CommonItem icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(Consumer<List<ICommonItem>> consumer) {
            consumer.accept(displayItems);
            return this;
        }

        public CommonCreativeTab build() {
            return new CommonCreativeTab(this);
        }
    }

    public CommonIdentifier identifier() {
        return identifier;
    }

    public String titleTranslationKey() {
        return titleTranslationKey;
    }

    public CommonItem icon() {
        return icon;
    }

    public List<ICommonItem> displayItems() {
        return displayItems;
    }
}
