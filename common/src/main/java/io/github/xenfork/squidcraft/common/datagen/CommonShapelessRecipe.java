package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;
import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.item.CommonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class CommonShapelessRecipe {
    private final CommonRecipeCategory category;
    private final CommonIdentifier output;
    private final int count;
    private final List<CommonIdentifier> ingredients = new ArrayList<>();

    public CommonShapelessRecipe(CommonRecipeCategory category, CommonItem output, int count) {
        this.category = category;
        this.output = output.identifier();
        this.count = count;
    }

    public CommonShapelessRecipe input(CommonIdentifiable item) {
        ingredients.add(item.identifier());
        return this;
    }

    public CommonRecipeCategory category() {
        return category;
    }

    public CommonIdentifier output() {
        return output;
    }

    public int count() {
        return count;
    }

    public List<CommonIdentifier> ingredients() {
        return ingredients;
    }
}
