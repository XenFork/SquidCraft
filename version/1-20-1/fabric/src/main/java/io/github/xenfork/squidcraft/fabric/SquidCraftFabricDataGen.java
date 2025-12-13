package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import io.github.xenfork.squidcraft.common.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

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
                    language.creativeTabs().forEach((id, name) -> translationBuilder.add(RegUtil.key(Registries.CREATIVE_MODE_TAB, id), name));
                }
            });
        }

        pack.addProvider((output, registriesFuture) -> new FabricTagProvider.ItemTagProvider(output, registriesFuture) {
            @Override
            protected void addTags(HolderLookup.Provider wrapperLookup) {
                for (CommonTagProvider itemTag : common.itemTags()) {
                    var builder = getOrCreateTagBuilder(RegUtil.tagKey(Registries.ITEM, itemTag.identifier()));
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
                        dropSelf(RegUtil.block(identifier));
                    }
                }
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricRecipeProvider(output) {
            @Override
            public void buildRecipes(Consumer<FinishedRecipe> exporter) {
                for (CommonReversibleCompactingRecipe recipe : common.reversibleCompactingRecipes()) {
                    nineBlockStorageRecipes(
                        exporter,
                        recipeCategory(recipe.reverseCategory()),
                        RegUtil.item(recipe.baseItem()),
                        recipeCategory(recipe.compactingCategory()),
                        RegUtil.item(recipe.compactItem())
                    );
                }

                for (CommonShapelessRecipe recipe : common.shapelessRecipes()) {
                    ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(recipeCategory(recipe.category()),
                        RegUtil.item(recipe.output()),
                        recipe.count());
                    List<String> put = new ArrayList<>(recipe.ingredients().size());
                    for (CommonIdentifier ingredient : recipe.ingredients()) {
                        Item item = RegUtil.item(ingredient);
                        builder.requires(item);
                        String criterion = getHasName(item);
                        if (!put.contains(criterion)) {
                            builder.unlockedBy(criterion, has(item));
                            put.add(criterion);
                        }
                    }
                    builder.save(exporter);
                }

                for (CommonFoodRecipe recipe : common.foodRecipes()) {
                    offerCookRecipe(recipe, recipe.baseCookingTime(), SimpleCookingRecipeBuilder::smelting, exporter, "smelting");
                    offerCookRecipe(recipe, recipe.baseCookingTime() / 2, SimpleCookingRecipeBuilder::smoking, exporter, "smoking");
                    offerCookRecipe(recipe, recipe.baseCookingTime() * 3, SimpleCookingRecipeBuilder::campfireCooking, exporter, "campfire_cooking");
                }
            }

            @Override
            public String getName() {
                return "SquidCraftFabricDataGen$RecipeProvider";
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricModelProvider(output) {
            @Override
            public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
                CommonModelProvider modelProvider = common.modelProvider();
                for (CommonIdentifier identifier : modelProvider.blockCubeAll()) {
                    blockStateModelGenerator.createTrivialCube(RegUtil.block(identifier));
                }
            }

            @Override
            public void generateItemModels(ItemModelGenerators itemModelGenerator) {
                CommonModelProvider modelProvider = common.modelProvider();
                for (CommonIdentifier identifier : modelProvider.itemGenerated()) {
                    itemModelGenerator.generateFlatItem(RegUtil.item(identifier), ModelTemplates.FLAT_ITEM);
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
        SimpleCookingRecipeBuilder create(Ingredient input, RecipeCategory category, ItemLike output, float experience, int cookingTime);
    }

    private static void offerCookRecipe(
        CommonFoodRecipe recipe,
        int cookingTime,
        CookingRecipeFactory factory,
        Consumer<FinishedRecipe> exporter,
        String method
    ) {
        Item output = RegUtil.item(recipe.output());
        SimpleCookingRecipeBuilder builder = factory.create(Ingredient.of(recipe.ingredients().stream()
                .map(RegUtil::item)
                .toArray(ItemLike[]::new)),
            recipeCategory(recipe.category()),
            output,
            recipe.experience(),
            cookingTime);
        for (CommonIdentifier ingredient : recipe.ingredients()) {
            Item item = RegUtil.item(ingredient);
            builder.unlockedBy(RecipeProvider.getHasName(item),
                RecipeProvider.inventoryTrigger(ItemPredicate.Builder.item().of(item).build()));
        }
        builder.save(exporter, SquidCraftCommon.MOD_ID + ":" + RecipeProvider.getItemName(output) + "_from_" + method);
    }
}
