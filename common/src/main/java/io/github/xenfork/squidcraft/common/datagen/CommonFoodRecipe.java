package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;
import io.github.xenfork.squidcraft.common.CommonIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class CommonFoodRecipe {
    private final CommonRecipeCategory category;
    private final List<CommonIdentifier> ingredients = new ArrayList<>();
    private final CommonIdentifier output;
    private final float experience;
    private final int baseCookingTime;

    public CommonFoodRecipe(CommonRecipeCategory category, CommonIdentifier output, float experience, int baseCookingTime) {
        this.category = category;
        this.output = output;
        this.experience = experience;
        this.baseCookingTime = baseCookingTime;
    }

    public CommonFoodRecipe input(CommonIdentifiable identifiable) {
        ingredients.add(identifiable.identifier());
        return this;
    }

    public CommonRecipeCategory category() {
        return category;
    }

    public List<CommonIdentifier> ingredients() {
        return ingredients;
    }

    public CommonIdentifier output() {
        return output;
    }

    public float experience() {
        return experience;
    }

    public int baseCookingTime() {
        return baseCookingTime;
    }
}
