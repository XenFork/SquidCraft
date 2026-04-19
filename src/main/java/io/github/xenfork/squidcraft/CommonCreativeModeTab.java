package io.github.xenfork.squidcraft;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/// @since 26.1.0
public class CommonCreativeModeTab {
    public final Component title;
    public final Supplier<ItemStack> icon;
    public final DisplayItemsGenerator displayItems;

    private CommonCreativeModeTab(
        Component title,
        Supplier<ItemStack> icon,
        DisplayItemsGenerator displayItems
    ) {
        this.title = title;
        this.icon = icon;
        this.displayItems = displayItems;
    }

    @FunctionalInterface
    public interface DisplayItemsGenerator {
        void accept(CreativeModeTab.ItemDisplayParameters parameters, Output output);
    }

    public interface Output {
        void accept(ItemStack itemStack);

        default void accept(ItemLike itemLike) {
            accept(new ItemStack(itemLike));
        }
    }

    public static class Builder {
        private Component title;
        private Supplier<ItemStack> icon;
        private DisplayItemsGenerator displayItems;

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder icon(Supplier<ItemStack> icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(DisplayItemsGenerator displayItems) {
            this.displayItems = displayItems;
            return this;
        }

        public CommonCreativeModeTab build() {
            return new CommonCreativeModeTab(title, icon, displayItems);
        }
    }
}
