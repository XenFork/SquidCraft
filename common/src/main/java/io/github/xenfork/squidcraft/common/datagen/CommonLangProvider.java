package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.block.CommonBlock;
import io.github.xenfork.squidcraft.common.item.CommonCreativeTab;
import io.github.xenfork.squidcraft.common.item.CommonItem;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.14.0
 */
public final class CommonLangProvider {
    private final String languageCode;
    private final Map<String, String> values = new HashMap<>();
    private final Map<CommonIdentifier, String> items = new HashMap<>();
    private final Map<CommonIdentifier, String> blocks = new HashMap<>();
    private final Map<CommonIdentifier, String> creativeTabs = new HashMap<>();

    public CommonLangProvider(String languageCode) {
        this.languageCode = languageCode;
    }

    public void addValue(String key, String value) {
        values.put(key, value);
    }

    public void add(CommonItem item, String name) {
        items.put(item.identifier(), name);
    }

    public void add(CommonBlock block, String name) {
        blocks.put(block.identifier(), name);
    }

    public void add(CommonCreativeTab creativeTab, String name) {
        creativeTabs.put(creativeTab.identifier(), name);
    }

    public String languageCode() {
        return languageCode;
    }

    public Map<String, String> values() {
        return values;
    }

    public Map<CommonIdentifier, String> items() {
        return items;
    }

    public Map<CommonIdentifier, String> blocks() {
        return blocks;
    }

    public Map<CommonIdentifier, String> creativeTabs() {
        return creativeTabs;
    }
}
