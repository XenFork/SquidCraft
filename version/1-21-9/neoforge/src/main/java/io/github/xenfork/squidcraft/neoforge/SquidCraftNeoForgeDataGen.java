package io.github.xenfork.squidcraft.neoforge;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.SquidCraftCommon;
import io.github.xenfork.squidcraft.common.datagen.*;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;

/**
 * @since 0.14.0
 */
public final class SquidCraftNeoForgeDataGen {
    @SubscribeEvent
    public static void onDataGen(GatherDataEvent.Client event) {
        SquidCraftCommonDataGen common = new SquidCraftCommonDataGen();
        common.initialize();

        for (CommonLangProvider language : common.languages()) {
            event.createProvider((output, lookupProvider) -> new LanguageProvider(output, SquidCraftCommon.MOD_ID, language.languageCode()) {
                @Override
                protected void addTranslations() {
                    language.values().forEach(this::add);
                    language.items().forEach((id, name) -> add(RegUtil.item(id), name));
                    language.blocks().forEach((id, name) -> add(RegUtil.block(id), name));
                    language.creativeTabs().forEach((id, name) -> {
                        ComponentContents displayName = RegUtil.get(BuiltInRegistries.CREATIVE_MODE_TAB, id).getDisplayName().getContents();
                        if (displayName instanceof TranslatableContents translatableContents) {
                            add(translatableContents.getKey(), name);
                        }
                    });
                }
            });
        }

        event.createBlockAndItemTags((output, lookupProvider) -> new BlockTagsProvider(output, lookupProvider, SquidCraftCommon.MOD_ID) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
            }
        }, (output, lookupProvider, contentsGetter) -> new ItemTagsProvider(output, lookupProvider, SquidCraftCommon.MOD_ID) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                for (CommonTagProvider itemTag : common.itemTags()) {
                    var builder = tag(RegUtil.tagKey(Registries.ITEM, itemTag.identifier()));
                    for (CommonIdentifier identifier : itemTag.values()) {
                        builder.add(RegUtil.item(identifier));
                    }
                }
            }
        });

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
            output,
            Set.of(),
            List.of(new LootTableProvider.SubProviderEntry(
                provider -> new BlockLootSubProvider(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider) {
                    @Override
                    protected Iterable<Block> getKnownBlocks() {
                        return SquidCraftNeoForge.BLOCKS.getEntries()
                            .stream()
                            .<Block>map(DeferredHolder::value)
                            .toList();
                    }

                    @Override
                    protected void generate() {
                        for (CommonBlockLootTableProvider lootTable : common.blockLootTables()) {
                            for (CommonIdentifier identifier : lootTable.drops()) {
                                dropSelf(RegUtil.block(identifier));
                            }
                        }
                    }
                },
                LootContextParamSets.BLOCK
            )),
            lookupProvider));

        event.createProvider((output, lookupProvider) -> new RecipeProvider.Runner(output, lookupProvider) {
            @Override
            protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
                return new MyRecipeProvider(registries, output, common);
            }

            @Override
            public String getName() {
                return "SquidCraftForgeDataGen$RecipeProvider";
            }
        });

        event.createProvider((output, lookupProvider) -> new ModelProvider(output, SquidCraftCommon.MOD_ID) {
            @Override
            protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
                CommonModelProvider modelProvider = common.modelProvider();
                for (CommonIdentifier identifier : modelProvider.blockCubeAll()) {
                    blockModels.createTrivialCube(RegUtil.block(identifier));
                }
                for (CommonIdentifier identifier : modelProvider.itemGenerated()) {
                    itemModels.generateFlatItem(RegUtil.item(identifier), ModelTemplates.FLAT_ITEM);
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

    private static final class MyRecipeProvider extends RecipeProvider {
        private final SquidCraftCommonDataGen common;

        public MyRecipeProvider(HolderLookup.Provider registries, RecipeOutput output, SquidCraftCommonDataGen common) {
            super(registries, output);
            this.common = common;
        }

        @Override
        protected void buildRecipes() {
            for (CommonReversibleCompactingRecipe recipe : common.reversibleCompactingRecipes()) {
                nineBlockStorageRecipes(
                    recipeCategory(recipe.reverseCategory()),
                    RegUtil.item(recipe.baseItem()),
                    recipeCategory(recipe.compactingCategory()),
                    RegUtil.item(recipe.compactItem())
                );
            }

            for (CommonShapelessRecipe recipe : common.shapelessRecipes()) {
                ShapelessRecipeBuilder builder = shapeless(recipeCategory(recipe.category()),
                    RegUtil.item(recipe.output()),
                    recipe.count());
                for (CommonIdentifier ingredient : recipe.ingredients()) {
                    Item item = RegUtil.item(ingredient);
                    builder.requires(item);
                    builder.unlockedBy(getHasName(item), has(item));
                }
                builder.save(output);
            }

            for (CommonFoodRecipe recipe : common.foodRecipes()) {
                offerCookRecipe(recipe, recipe.baseCookingTime(), SimpleCookingRecipeBuilder::smelting, registries, output, "smelting");
                offerCookRecipe(recipe, recipe.baseCookingTime() / 2, SimpleCookingRecipeBuilder::smoking, registries, output, "smoking");
                offerCookRecipe(recipe, recipe.baseCookingTime() * 3, SimpleCookingRecipeBuilder::campfireCooking, registries, output, "campfire_cooking");
            }
        }


        private static void offerCookRecipe(
            CommonFoodRecipe recipe,
            int cookingTime,
            CookingRecipeFactory factory,
            HolderLookup.Provider registries,
            RecipeOutput output,
            String method
        ) {
            Item outputItem = RegUtil.item(recipe.output());
            SimpleCookingRecipeBuilder builder = factory.create(Ingredient.of(recipe.ingredients().stream()
                    .map(RegUtil::item)),
                recipeCategory(recipe.category()),
                outputItem,
                recipe.experience(),
                cookingTime);
            for (CommonIdentifier ingredient : recipe.ingredients()) {
                Item item = RegUtil.item(ingredient);
                builder.unlockedBy(RecipeProvider.getHasName(item),
                    RecipeProvider.inventoryTrigger(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), item)));
            }
            builder.save(output, SquidCraftCommon.MOD_ID + ":" + RecipeProvider.getItemName(outputItem) + "_from_" + method);
        }
    }
}
