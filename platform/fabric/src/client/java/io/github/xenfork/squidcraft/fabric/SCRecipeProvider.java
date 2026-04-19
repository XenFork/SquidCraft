package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftCommon;
import io.github.xenfork.squidcraft.SquidCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// @since 26.1.0
public class SCRecipeProvider extends FabricRecipeProvider {
    public SCRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                nineBlockStorageRecipes(RecipeCategory.FOOD,
                    SquidCraftItems.COOKED_SHREDDED_SQUID,
                    RecipeCategory.MISC,
                    SquidCraftItems.COOKED_SHREDDED_SQUID_BLOCK);

                shapeless(RecipeCategory.FOOD, SquidCraftItems.MAGMA_SHREDDED_SQUID, 2)
                    .requires(SquidCraftItems.COOKED_SHREDDED_SQUID, 2)
                    .requires(Items.MAGMA_CREAM)
                    .unlockedBy(getHasName(SquidCraftItems.COOKED_SHREDDED_SQUID), has(SquidCraftItems.COOKED_SHREDDED_SQUID))
                    .unlockedBy(getHasName(Items.MAGMA_CREAM), has(Items.MAGMA_CREAM))
                    .save(output);

                cookFoodRecipe(List.of(SquidCraftItems.SHREDDED_SQUID, SquidCraftItems.GLOW_SHREDDED_SQUID),
                    SquidCraftItems.COOKED_SHREDDED_SQUID,
                    0.35f,
                    100);
            }

            private void cookFoodRecipe(
                List<ItemLike> ingredient,
                ItemLike result,
                float experience,
                int cookingTime,
                AbstractCookingRecipe.Factory<? extends AbstractCookingRecipe> factory,
                String method
            ) {
                SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder.generic(Ingredient.of(ingredient.stream()),
                    RecipeCategory.FOOD,
                    CookingBookCategory.FOOD,
                    result,
                    experience,
                    cookingTime,
                    factory
                );
                for (ItemLike itemLike : ingredient) {
                    builder.unlockedBy(getHasName(itemLike), has(itemLike));
                }
                builder.save(output, SquidCraftCommon.MOD_ID + ":" + RecipeProvider.getItemName(result) + "_from_" + method);
            }

            private void cookFoodRecipe(
                List<ItemLike> ingredient,
                ItemLike result,
                float experience,
                int baseCookingTime
            ) {
                cookFoodRecipe(ingredient, result, experience, baseCookingTime, SmeltingRecipe::new, "smelting");
                cookFoodRecipe(ingredient, result, experience, baseCookingTime / 2, SmokingRecipe::new, "smoking");
                cookFoodRecipe(ingredient, result, experience, baseCookingTime * 3, CampfireCookingRecipe::new, "campfire_cooking");
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return getClass().getName();
    }
}
