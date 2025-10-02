package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import io.github.xenfork.squidcraft.common.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @since 0.14.0
 */
public class SquidCraftFabricDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        SquidCraftCommonDataGen common = new SquidCraftCommonDataGen();
        common.initialize();
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        for (CommonLangProvider language : common.languages()) {
            pack.addProvider((output, registriesFuture) -> new FabricLanguageProvider(output, language.languageCode()) {
                @Override
                public void generateTranslations(TranslationBuilder translationBuilder) {
                    language.values().forEach(translationBuilder::add);
                    language.items().forEach((id, name) -> translationBuilder.add(RegUtil.item(id), name));
                    language.blocks().forEach((id, name) -> translationBuilder.add(RegUtil.block(id), name));
                    language.creativeTabs().forEach((id, name) -> translationBuilder.add(RegUtil.key(RegistryKeys.ITEM_GROUP, id), name));
                }
            });
        }

        pack.addProvider((output, registriesFuture) -> new FabricTagProvider.ItemTagProvider(output, registriesFuture) {
            @Override
            protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
                for (CommonTagProvider itemTag : common.itemTags()) {
                    var builder = getOrCreateTagBuilder(RegUtil.tagKey(RegistryKeys.ITEM, itemTag.identifier()));
                    for (CommonIdentifier identifier : itemTag.values()) {
                        builder.add(RegUtil.item(identifier));
                    }
                }
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricBlockLootTableProvider(output) {
            @Override
            public void generate() {
                for (CommonBlockLootTableProvider lootTable : common.blockLootTables()) {
                    for (CommonIdentifier identifier : lootTable.drops()) {
                        addDrop(RegUtil.block(identifier));
                    }
                }
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricRecipeProvider(output) {
            @Override
            public void generate(Consumer<RecipeJsonProvider> exporter) {
                for (CommonReversibleCompactingRecipe recipe : common.reversibleCompactingRecipes()) {
                    offerReversibleCompactingRecipes(
                        exporter,
                        recipeCategory(recipe.reverseCategory()),
                        RegUtil.item(recipe.baseItem()),
                        recipeCategory(recipe.compactingCategory()),
                        RegUtil.item(recipe.compactItem())
                    );
                }

                for (CommonShapelessRecipe recipe : common.shapelessRecipes()) {
                    ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(recipeCategory(recipe.category()),
                        RegUtil.item(recipe.output()),
                        recipe.count());
                    List<String> put = new ArrayList<>(recipe.ingredients().size());
                    for (CommonIdentifier ingredient : recipe.ingredients()) {
                        Item item = RegUtil.item(ingredient);
                        builder.input(item);
                        String criterion = hasItem(item);
                        if (!put.contains(criterion)) {
                            builder.criterion(criterion, conditionsFromItem(item));
                            put.add(criterion);
                        }
                    }
                    builder.offerTo(exporter);
                }

                for (CommonFoodRecipe recipe : common.foodRecipes()) {
                    offerCookRecipe(recipe, recipe.baseCookingTime(), CookingRecipeJsonBuilder::createSmelting, exporter, "smelting");
                    offerCookRecipe(recipe, recipe.baseCookingTime() / 2, CookingRecipeJsonBuilder::createSmoking, exporter, "smoking");
                    offerCookRecipe(recipe, recipe.baseCookingTime() * 3, CookingRecipeJsonBuilder::createCampfireCooking, exporter, "campfire_cooking");
                }
            }

            @Override
            public String getName() {
                return "SquidCraftFabricDataGen$RecipeProvider";
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricModelProvider(output) {
            @Override
            public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
                CommonModelProvider modelProvider = common.modelProvider();
                for (CommonIdentifier identifier : modelProvider.blockCubeAll()) {
                    blockStateModelGenerator.registerSimpleCubeAll(RegUtil.block(identifier));
                }
            }

            @Override
            public void generateItemModels(ItemModelGenerator itemModelGenerator) {
                CommonModelProvider modelProvider = common.modelProvider();
                for (CommonIdentifier identifier : modelProvider.itemGenerated()) {
                    itemModelGenerator.register(RegUtil.item(identifier), Models.GENERATED);
                }
            }
        });
    }

    private static RecipeCategory recipeCategory(CommonRecipeCategory category) {
        return switch (category) {
            case FOOD -> RecipeCategory.FOOD;
            case MISC -> RecipeCategory.MISC;
        };
    }

    @FunctionalInterface
    private interface CookingRecipeFactory {
        CookingRecipeJsonBuilder create(Ingredient input, RecipeCategory category, ItemConvertible output, float experience, int cookingTime);
    }

    private static void offerCookRecipe(
        CommonFoodRecipe recipe,
        int cookingTime,
        CookingRecipeFactory factory,
        Consumer<RecipeJsonProvider> exporter,
        String method
    ) {
        Item output = RegUtil.item(recipe.output());
        CookingRecipeJsonBuilder builder = factory.create(Ingredient.ofItems(recipe.ingredients().stream()
                .map(RegUtil::item)
                .toArray(ItemConvertible[]::new)),
            recipeCategory(recipe.category()),
            output,
            recipe.experience(),
            cookingTime);
        for (CommonIdentifier ingredient : recipe.ingredients()) {
            Item item = RegUtil.item(ingredient);
            builder.criterion(RecipeProvider.hasItem(item),
                RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create().items(item).build()));
        }
        builder.offerTo(exporter, SquidCraftCommon.MOD_ID + ":" + RecipeProvider.getItemPath(output) + "_from_" + method);
    }
}
