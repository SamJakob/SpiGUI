package com.samjakob.spigui.item;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A helper for creating and modifying {@link ItemStack}s.
 *
 * <p>In future, this class will be removed and replaced with {@link ItemBuilderBase}. Presently, this class exists for
 * backwards-compatibility, so that previous consumers of the {@link ItemBuilder} API (who would have initialized an
 * ItemBuilder with a constructor) can have a phased move to using the static factory methods instead.
 *
 * <p>This class provides a convenient chainable ('builder pattern') API for manipulating the metadata of an
 * {@link ItemStack}, replacing several calls into a clean one-liner in many cases.
 *
 * <p><b>Note:</b> as a convention (to make debugging and identifying potential issues simpler) all methods on this
 * interface shall throw a {@link NullPointerException} if the item meta is null. This would only happen if Bukkit's API
 * failed to produce an {@link ItemMeta} object for an item type - which shouldn't ever happen.
 *
 * <pre>{@code
 * final var sponge = ItemBuilder.create(Material.SPONGE).name("&amp;cAlmighty sponge").amount(21).build();
 * }</pre>
 *
 * @author SamJakob
 * @version 3.0.0
 * @see ItemBuilderBase
 * @see ItemStack
 */
public final class ItemBuilder implements ItemBuilderBase {

    /** The underlying {@link ItemBuilderBase} that this wrapper will delegate to. */
    private final ItemBuilderBase builder;

    /**
     * Create an {@link ItemBuilder} (wrapper for {@link ItemBuilderBase}).
     *
     * @param builder (underlying {@link ItemBuilderBase}) to wrap.
     */
    private ItemBuilder(ItemBuilderBase builder) {
        this.builder = builder;
    }

    /**
     * Create a new {@link ItemBuilder} for the given {@link Material} type.
     *
     * <p>This method is deprecated and will be replaced with {@link ItemBuilder#create(Material)}. The ultimate goal is
     * to replace the {@link ItemBuilder} class with the {@link ItemBuilderBase} interface.
     *
     * @throws IllegalArgumentException if the material is a non-item type.
     * @param material type of stack to create a builder for.
     * @deprecated use {@link ItemBuilder#create(Material)} as this method is to be replaced with static factory method
     *     - see above.
     */
    @Deprecated()
    public ItemBuilder(@Nonnull Material material) {
        this.builder = ItemBuilderBase.create(material);
    }

    /**
     * Create a new {@link ItemBuilder} for the given {@link Material} type.
     *
     * @throws IllegalArgumentException if the material is a non-item type.
     * @param material type of stack to create a builder for.
     * @return the constructed {@link ItemBuilder}.
     */
    public static ItemBuilder create(@Nonnull Material material) {
        return new ItemBuilder(ItemBuilderBase.create(material));
    }

    /**
     * Create a new {@link ItemBuilder} that uses the provided {@link ItemStack} and associated metadata as the initial
     * configuration.
     *
     * <p>This method is deprecated and will be replaced with {@link ItemBuilder#from(ItemStack)}. The ultimate goal is
     * to replace the {@link ItemBuilder} class with the {@link ItemBuilderBase} interface.
     *
     * @throws IllegalArgumentException if the item stack's type (material) is a non-item type.
     * @param stack to derive the builder options from.
     * @deprecated use {@link ItemBuilder#from(ItemStack)} as this method is to be replaced with static factory method -
     *     see above.
     */
    @Deprecated()
    public ItemBuilder(@Nonnull ItemStack stack) {
        this.builder = ItemBuilderBase.from(stack);
    }

    /**
     * Create a new {@link ItemBuilder} that uses the provided {@link ItemStack} and associated metadata as the initial
     * configuration.
     *
     * @throws IllegalArgumentException if the item stack's type (material) is a non-item type.
     * @param stack to derive the builder options from.
     * @return the constructed {@link ItemBuilder}.
     */
    public static ItemBuilder from(@Nonnull ItemStack stack) {
        return new ItemBuilder(ItemBuilderBase.from(stack));
    }

    @Nonnull
    @Override
    public ItemBuilder type(@Nonnull Material material) {
        builder.type(material);
        return this;
    }

    @Nonnull
    @Override
    public Material getType() {
        return builder.getType();
    }

    @Nonnull
    @Override
    public ItemBuilder name(@Nullable String name) {
        builder.name(name);
        return this;
    }

    @Nullable
    @Override
    public String getName() {
        return builder.getName();
    }

    @Nonnull
    @Override
    public ItemBuilder amount(int amount) {
        builder.amount(amount);
        return this;
    }

    @Override
    public int getAmount() {
        return builder.getAmount();
    }

    @Nonnull
    @Override
    public ItemBuilder lore(@Nullable String... lore) {
        builder.lore(lore);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder lore(@Nullable List<String> lore) {
        builder.lore(lore);
        return this;
    }

    @Nullable
    @Override
    public List<String> getLore() {
        return builder.getLore();
    }

    @Nonnull
    @Override
    public ItemBuilder color(@Nonnull ItemColor color) {
        builder.color(color);
        return this;
    }

    @Nullable
    @Override
    public ItemColor getColor() {
        return builder.getColor();
    }

    @Nonnull
    @Override
    public ItemBuilder data(short data) {
        builder.data(data);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder durability(int durability) {
        builder.durability(durability);
        return this;
    }

    @Override
    public int getDurability() {
        return builder.getDurability();
    }

    @Nonnull
    @Override
    public ItemBuilderBase maxDurability(int maxDurability) {
        builder.maxDurability(maxDurability);
        return this;
    }

    @Override
    public int getMaxDurability() {
        return builder.getMaxDurability();
    }

    @Nonnull
    @Override
    public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level) {
        builder.enchant(enchantment, level);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder unenchant(@Nonnull Enchantment enchantment) {
        builder.unenchant(enchantment);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder flag(@Nonnull ItemFlag... flag) {
        builder.flag(flag);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder deflag(@Nonnull ItemFlag... flag) {
        builder.deflag(flag);
        return this;
    }

    @Nonnull
    @Override
    @Deprecated
    public ItemBuilderBase skullOwner(@Nullable String name) {
        builder.skullOwner(name);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable UUID uuid) {
        builder.skullOwner(uuid);
        return this;
    }

    @Nonnull
    @Override
    public ItemStack build() {
        return builder.build();
    }
}
