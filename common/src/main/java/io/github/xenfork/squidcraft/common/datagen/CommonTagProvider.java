package io.github.xenfork.squidcraft.common.datagen;

import io.github.xenfork.squidcraft.common.CommonIdentifier;
import io.github.xenfork.squidcraft.common.item.CommonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.14.0
 */
public final class CommonTagProvider {
    private final CommonIdentifier identifier;
    private final List<CommonIdentifier> values = new ArrayList<>();

    public CommonTagProvider(CommonIdentifier identifier) {
        this.identifier = identifier;
    }

    public void add(CommonIdentifier identifier) {
        values.add(identifier);
    }

    public void add(CommonItem item) {
        add(item.identifier());
    }

    public CommonIdentifier identifier() {
        return identifier;
    }

    public List<CommonIdentifier> values() {
        return values;
    }
}
