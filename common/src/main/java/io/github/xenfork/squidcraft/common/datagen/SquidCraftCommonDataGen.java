package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlocks;
import io.github.xenfork.squidcraft.common.item.CommonCreativeTabs;
import io.github.xenfork.squidcraft.common.item.CommonItem;
import io.github.xenfork.squidcraft.common.item.CommonItems;
import io.github.xenfork.squidcraft.common.item.VanillaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class SquidCraftCommonDataGen {
    private final List<CommonLangProvider> languages = new ArrayList<>();
    private final List<CommonTagProvider> itemTags = new ArrayList<>();
    private final List<CommonBlockLootTableProvider> blockLootTables = new ArrayList<>();
    private final List<CommonReversibleCompactingRecipe> reversibleCompactingRecipes = new ArrayList<>();
    private final List<CommonShapelessRecipe> shapelessRecipes = new ArrayList<>();
    private final List<CommonFoodRecipe> foodRecipes = new ArrayList<>();
    private final CommonModelProvider modelProvider = new CommonModelProvider();

    public void initialize() {
        initializeLangEnUs();
        initializeLangZhCn();
        initializeTags();
        initializeLootTables();
        initializeRecipes();
        initializeModels();
    }

    private void initializeLangEnUs() {
        CommonLangProvider lang = createLang("en_us");
        lang.addValue("advancements.squidcraft.root.title", "SquidCraft");
        lang.addValue("advancements.squidcraft.root.description", "Get shredded squid");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid.title", "Cooked Shredded Squid");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid.description", "Get cooked shredded squid");
        lang.addValue("advancements.squidcraft.glow_shredded_squid.title", "Glow Shredded Squid");
        lang.addValue("advancements.squidcraft.glow_shredded_squid.description", "Get glow shredded squid");
        lang.addValue("advancements.squidcraft.magma_shredded_squid.title", "Magma Shredded Squid");
        lang.addValue("advancements.squidcraft.magma_shredded_squid.description", "Get magma shredded squid");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid_block.title", "Block of Cooked Shredded Squid");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid_block.description", "Get block of cooked shredded squid");
        lang.add(CommonBlocks.COOKED_SHREDDED_SQUID_BLOCK, "Block of Cooked Shredded Squid");
        lang.add(CommonItems.SHREDDED_SQUID, "Shredded Squid");
        lang.add(CommonItems.COOKED_SHREDDED_SQUID, "Cooked Shredded Squid");
        lang.add(CommonItems.GLOW_SHREDDED_SQUID, "Glow Shredded Squid");
        lang.add(CommonItems.MAGMA_SHREDDED_SQUID, "Magma Shredded Squid");
        lang.add(CommonCreativeTabs.MAIN, "SquidCraft");
        lang.addValue("modmenu.nameTranslation.squidcraft", "SquidCraft");
        lang.addValue("modmenu.descriptionTranslation.squidcraft", "Make squids useful.");
    }

    private void initializeLangZhCn() {
        CommonLangProvider lang = createLang("zh_cn");
        lang.addValue("advancements.squidcraft.root.title", "鱿鱼工艺");
        lang.addValue("advancements.squidcraft.root.description", "获得鱿鱼丝");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid.title", "熟鱿鱼丝");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid.description", "获得熟鱿鱼丝");
        lang.addValue("advancements.squidcraft.glow_shredded_squid.title", "荧光鱿鱼丝");
        lang.addValue("advancements.squidcraft.glow_shredded_squid.description", "获得荧光鱿鱼丝");
        lang.addValue("advancements.squidcraft.magma_shredded_squid.title", "岩浆鱿鱼丝");
        lang.addValue("advancements.squidcraft.magma_shredded_squid.description", "获得岩浆鱿鱼丝");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid_block.title", "熟鱿鱼丝块");
        lang.addValue("advancements.squidcraft.cooked_shredded_squid_block.description", "获得熟鱿鱼丝块");
        lang.add(CommonBlocks.COOKED_SHREDDED_SQUID_BLOCK, "熟鱿鱼丝块");
        lang.add(CommonItems.SHREDDED_SQUID, "鱿鱼丝");
        lang.add(CommonItems.COOKED_SHREDDED_SQUID, "熟鱿鱼丝");
        lang.add(CommonItems.GLOW_SHREDDED_SQUID, "荧光鱿鱼丝");
        lang.add(CommonItems.MAGMA_SHREDDED_SQUID, "岩浆鱿鱼丝");
        lang.add(CommonCreativeTabs.MAIN, "鱿鱼工艺");
        lang.addValue("modmenu.nameTranslation.squidcraft", "鱿鱼工艺");
        lang.addValue("modmenu.descriptionTranslation.squidcraft", "让鱿鱼更有用");
    }

    private void initializeTags() {
        addRawFish("c", "foods/raw_fishes");
        addRawFish("c", "foods/raw_fish");

        CommonTagProvider meat = createItemTag(new CommonIdentifier("minecraft", "meat"));
        meat.add(CommonItems.SHREDDED_SQUID);
        meat.add(CommonItems.COOKED_SHREDDED_SQUID);
        meat.add(CommonItems.GLOW_SHREDDED_SQUID);
        meat.add(CommonItems.MAGMA_SHREDDED_SQUID);
    }

    private void initializeLootTables() {
        CommonBlockLootTableProvider provider = new CommonBlockLootTableProvider();
        blockLootTables.add(provider);
        provider.add(CommonBlocks.COOKED_SHREDDED_SQUID_BLOCK);
    }

    private void initializeRecipes() {
        createReversibleCompactingRecipe(CommonRecipeCategory.FOOD,
            CommonItems.COOKED_SHREDDED_SQUID,
            CommonRecipeCategory.MISC,
            CommonItems.COOKED_SHREDDED_SQUID_BLOCK);
        createShapelessRecipe(CommonRecipeCategory.FOOD, CommonItems.MAGMA_SHREDDED_SQUID, 2)
            .input(CommonItems.COOKED_SHREDDED_SQUID)
            .input(CommonItems.COOKED_SHREDDED_SQUID)
            .input(VanillaItem.MAGMA_CREAM);
        createFoodRecipe(CommonRecipeCategory.FOOD,
            CommonItems.COOKED_SHREDDED_SQUID,
            0.35f,
            100)
            .input(CommonItems.SHREDDED_SQUID)
            .input(CommonItems.GLOW_SHREDDED_SQUID);
    }

    private void initializeModels() {
        modelProvider.registerCubeAll(CommonBlocks.COOKED_SHREDDED_SQUID_BLOCK);
        modelProvider.registerGenerated(CommonItems.SHREDDED_SQUID);
        modelProvider.registerGenerated(CommonItems.COOKED_SHREDDED_SQUID);
        modelProvider.registerGenerated(CommonItems.GLOW_SHREDDED_SQUID);
        modelProvider.registerGenerated(CommonItems.MAGMA_SHREDDED_SQUID);
    }

    private void addRawFish(String namespace, String path) {
        CommonTagProvider tag = createItemTag(new CommonIdentifier(namespace, path));
        tag.add(CommonItems.SHREDDED_SQUID);
        tag.add(CommonItems.GLOW_SHREDDED_SQUID);
    }

    private CommonLangProvider createLang(String languageCode) {
        CommonLangProvider provider = new CommonLangProvider(languageCode);
        languages.add(provider);
        return provider;
    }

    private CommonTagProvider createItemTag(CommonIdentifier identifier) {
        CommonTagProvider provider = new CommonTagProvider(identifier);
        itemTags.add(provider);
        return provider;
    }

    private void createReversibleCompactingRecipe(
        CommonRecipeCategory reverseCategory,
        CommonItem baseItem,
        CommonRecipeCategory compactingCategory,
        CommonItem compactItem
    ) {
        CommonReversibleCompactingRecipe recipe = new CommonReversibleCompactingRecipe(reverseCategory, baseItem, compactingCategory, compactItem);
        reversibleCompactingRecipes.add(recipe);
    }

    private CommonShapelessRecipe createShapelessRecipe(CommonRecipeCategory category, CommonItem output, int count) {
        CommonShapelessRecipe recipe = new CommonShapelessRecipe(category, output, count);
        shapelessRecipes.add(recipe);
        return recipe;
    }

    private CommonFoodRecipe createFoodRecipe(CommonRecipeCategory category, CommonItem output, float experience, int baseCookingTime) {
        CommonFoodRecipe recipe = new CommonFoodRecipe(category, output.identifier(), experience, baseCookingTime);
        foodRecipes.add(recipe);
        return recipe;
    }

    public List<CommonLangProvider> languages() {
        return languages;
    }

    public List<CommonTagProvider> itemTags() {
        return itemTags;
    }

    public List<CommonBlockLootTableProvider> blockLootTables() {
        return blockLootTables;
    }

    public List<CommonReversibleCompactingRecipe> reversibleCompactingRecipes() {
        return reversibleCompactingRecipes;
    }

    public List<CommonShapelessRecipe> shapelessRecipes() {
        return shapelessRecipes;
    }

    public List<CommonFoodRecipe> foodRecipes() {
        return foodRecipes;
    }

    public CommonModelProvider modelProvider() {
        return modelProvider;
    }
}
