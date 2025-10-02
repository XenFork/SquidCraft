package io.github.xenfork.squidcraft.common.item;

import io.github.xenfork.squidcraft.common.CommonIdentifiable;

/**
 * @since 0.14.0
 */
public sealed interface ICommonItem extends CommonIdentifiable permits CommonItem, VanillaItem {
}
