package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import io.github.xenfork.squidcraft.common.datagen.*;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

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
            pack.addProvider((output, registriesFuture) -> new FabricLanguageProvider(output, language.languageCode(), registriesFuture) {
                @Override
                public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
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
                    var builder = valueLookupBuilder(RegUtil.tagKey(RegistryKeys.ITEM, itemTag.identifier()));
                    for (CommonIdentifier identifier : itemTag.values()) {
                        builder.add(RegUtil.item(identifier));
                    }
                }
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricBlockLootTableProvider(output, registriesFuture) {
            @Override
            public void generate() {
                for (CommonBlockLootTableProvider lootTable : common.blockLootTables()) {
                    for (CommonIdentifier identifier : lootTable.drops()) {
                        addDrop(RegUtil.block(identifier));
                    }
                }
            }
        });

        pack.addProvider((output, registriesFuture) -> new FabricRecipeProvider(output, registriesFuture) {
            @Override
            protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
                return new RecipeGenerator(registryLookup, exporter) {
                    @Override
                    public void generate() {
                        for (CommonReversibleCompactingRecipe recipe : common.reversibleCompactingRecipes()) {
                            offerReversibleCompactingRecipes(
                                recipeCategory(recipe.reverseCategory()),
                                RegUtil.item(recipe.baseItem()),
                                recipeCategory(recipe.compactingCategory()),
                                RegUtil.item(recipe.compactItem())
                            );
                        }

                        for (CommonShapelessRecipe recipe : common.shapelessRecipes()) {
                            ShapelessRecipeJsonBuilder builder = createShapeless(recipeCategory(recipe.category()),
                                RegUtil.item(recipe.output()),
                                recipe.count());
                            for (CommonIdentifier ingredient : recipe.ingredients()) {
                                Item item = RegUtil.item(ingredient);
                                builder.input(item);
                                builder.criterion(hasItem(item), conditionsFromItem(item));
                            }
                            builder.offerTo(exporter);
                        }

                        for (CommonFoodRecipe recipe : common.foodRecipes()) {
                            offerCookRecipe(recipe, recipe.baseCookingTime(), CookingRecipeJsonBuilder::createSmelting, registries, exporter, "smelting");
                            offerCookRecipe(recipe, recipe.baseCookingTime() / 2, CookingRecipeJsonBuilder::createSmoking, registries, exporter, "smoking");
                            offerCookRecipe(recipe, recipe.baseCookingTime() * 3, CookingRecipeJsonBuilder::createCampfireCooking, registries, exporter, "campfire_cooking");
                        }
                    }
                };
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
        RegistryWrapper.WrapperLookup registryLookup,
        RecipeExporter exporter,
        String method
    ) {
        Item output = RegUtil.item(recipe.output());
        CookingRecipeJsonBuilder builder = factory.create(Ingredient.ofItems(recipe.ingredients().stream()
                .map(RegUtil::item)),
            recipeCategory(recipe.category()),
            output,
            recipe.experience(),
            cookingTime);
        for (CommonIdentifier ingredient : recipe.ingredients()) {
            Item item = RegUtil.item(ingredient);
            builder.criterion(RecipeGenerator.hasItem(item),
                RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(registryLookup.getOrThrow(RegistryKeys.ITEM), item)));
        }
        builder.offerTo(exporter, SquidCraftCommon.MOD_ID + ":" + RecipeGenerator.getItemPath(output) + "_from_" + method);
    }
}
