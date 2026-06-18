package io.github.xenfork.squidcraft.fabric;

import io.github.xenfork.squidcraft.SquidCraftItems;
import io.github.xenfork.squidcraft.SquidCraftLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/// @since 26.1.0
public class SCLootTableProvider extends SimpleFabricLootTableSubProvider {
    private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;

    public SCLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture, LootContextParamSets.ALL_PARAMS);
        this.registryLookupFuture = registryLookupFuture;
    }

    @Override
    public void generate(@NonNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        add(output, SquidCraftLootTables.SHREDDED_SQUID, SquidCraftItems.SHREDDED_SQUID);
        add(output, SquidCraftLootTables.GLOW_SHREDDED_SQUID, SquidCraftItems.GLOW_SHREDDED_SQUID);
    }

    private void add(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output, ResourceKey<LootTable> key, Item item) {
        output.accept(
            key,
            LootTable.lootTable().withPool(createSquidLootPool(registryLookupFuture.join(), item))
        );
    }

    private static LootPool.Builder createSquidLootPool(HolderLookup.Provider registries, Item item) {
        return LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 8f)))
                .apply(SmeltItemFunction.smelted().when(() ->
                    AnyOfCondition.anyOf(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .flags(EntityFlagsPredicate.Builder.flags()
                                    .setOnFire(true))
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.DIRECT_ATTACKER,
                            EntityPredicate.Builder.entity()
                                .equipment(EntityEquipmentPredicate.Builder.equipment()
                                    .mainhand(ItemPredicate.Builder.item()
                                        .withComponents(DataComponentMatchers.Builder.components()
                                            .partial(DataComponentPredicates.ENCHANTMENTS,
                                                EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(
                                                    registries.lookup(Registries.ENCHANTMENT)
                                                        .flatMap(it ->
                                                            it.get(EnchantmentTags.SMELTS_LOOT)),
                                                    MinMaxBounds.Ints.ANY
                                                ))))
                                            .build())))
                        )
                    ).build())
                )
                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1f, 4f))));
    }
}
