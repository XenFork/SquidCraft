package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.item.CommonItem;

/**
 * @since 0.14.0
 */
public record CommonReversibleCompactingRecipe(
    CommonRecipeCategory reverseCategory,
    CommonIdentifier baseItem,
    CommonRecipeCategory compactingCategory,
    CommonIdentifier compactItem
) {
    public CommonReversibleCompactingRecipe(
        CommonRecipeCategory reverseCategory,
        CommonItem baseItem,
        CommonRecipeCategory compactingCategory,
        CommonItem compactItem
    ) {
        this(reverseCategory, baseItem.identifier(), compactingCategory, compactItem.identifier());
    }
}
