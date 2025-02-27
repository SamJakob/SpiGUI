package com.samjakob.spigui.item;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A helper for creating and modifying {@link ItemStack}s.
 *
 * <p><b>NOTE:</b> in future, this class will be renamed to ItemBuilder (see {@link ItemBuilder}). For now, you should
 * continue to use {@link ItemBuilder} as it is intended to be a seamless replacement.
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
 * @see ItemStack
 */
@SuppressWarnings("UnusedReturnValue")
public interface ItemBuilderBase {

    /**
     * Create a new {@link ItemBuilderBase} for the given {@link Material} type.
     *
     * @throws IllegalArgumentException if the material is a non-item type.
     * @param material type of stack to create a builder for.
     * @return the constructed {@link ItemBuilderBase}.
     */
    @Nonnull
    static ItemBuilderBase create(@Nonnull Material material) {
        return ItemBuilderFactory.get().create(material);
    }

    /**
     * Create a new {@link ItemBuilderBase} that uses the provided {@link ItemStack} and associated metadata as the
     * initial configuration.
     *
     * @throws IllegalArgumentException if the item stack's type (material) is a non-item type.
     * @param stack to derive the builder options from.
     * @return the constructed {@link ItemBuilderBase}.
     */
    @Nonnull
    static ItemBuilderBase from(@Nonnull ItemStack stack) {
        return ItemBuilderFactory.get().from(stack);
    }

    /**
     * Sets the type ({@link Material}) of the ItemStack.
     *
     * @param material The {@link Material} of the stack.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase type(@Nonnull Material material);

    /**
     * Returns the type ({@link Material}) of the ItemStack.
     *
     * @return The {@link Material} of the stack.
     */
    @Nonnull
    Material getType();

    /**
     * Sets the display name of the item.
     *
     * <p>Color codes using the ampersand ({@code &}) are translated, if you want to avoid this, you should wrap your
     * name argument with a {@link ChatColor#stripColor(String)} call.
     *
     * @param name The desired display name of the item stack.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase name(@Nullable String name);

    /**
     * Returns either the display name of the item, if it exists, or null if it doesn't.
     *
     * <p>You should note that this method fetches the name directly from the stack's {@link ItemMeta}, so you should
     * take extra care when comparing names with color codes - particularly if you used the {@link #name(String)} method
     * as they will be in their translated sectional symbol (§) form, rather than their 'coded' form ({@code &}).
     *
     * <p>For example, if you used {@link #name(String)} to set the name to '&amp;cMy Item', the output of this method
     * would be '§cMy Item'
     *
     * @return The item's display name as returned from its {@link ItemMeta}.
     */
    @Nullable
    String getName();

    /**
     * Sets the amount of items in the {@link ItemStack}.
     *
     * @param amount The new amount.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase amount(int amount);

    /**
     * Returns the amount of items in the {@link ItemStack}.
     *
     * @return The amount of items in the stack.
     */
    int getAmount();

    /**
     * Sets the lore of the item. This method is a var-args alias for the {@link #lore(List)} method.
     *
     * @param lore The desired lore of the item, with each line as a separate string.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase lore(@Nullable String... lore);

    /**
     * Sets the lore of the item. As with {@link #name(String)}, color codes will be replaced. Each string represents a
     * line of the lore.
     *
     * <p>Lines will not be automatically wrapped or truncated, so it is recommended you take some consideration into
     * how the item will be rendered with the lore.
     *
     * @param lore The desired lore of the item, with each line as a separate string.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase lore(@Nullable List<String> lore);

    /**
     * Gets the lore of the item as a list of strings. Each string represents a line of the item's lore in-game.
     *
     * <p>As with {@link #name(String)}, it should be noted that color-coded lore lines will be returned with the colors
     * codes already translated.
     *
     * @return The lore of the item.
     */
    @Nullable
    List<String> getLore();

    /**
     * Set the color of items (where those items can have a color applied to them).
     *
     * <p>The behavior of this method is undefined when an item does not have color values associated with it.
     *
     * @param color The desired color of the item.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase color(@Nonnull ItemColor color);

    /**
     * Returns the color of the item.
     *
     * <p>The behavior of this method is undefined when an item does not have color values associated with it and may be
     * inconsistent across versions of the game.
     *
     * @return The {@link ItemColor} of the item (where the item has a color associated).
     */
    @Nullable
    ItemColor getColor();

    /**
     * Set the data value of the item.
     *
     * <p>The behavior of this method is undefined when an item does not have a data value associated with it.
     *
     * <p>In newer versions of the game (Minecraft 1.13+), this method throws {@link UnsupportedOperationException} to
     * encourage use of a different method (or to open a GitHub issue if functionality has been missed).
     *
     * @param data data value for the item.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase data(short data);

    /**
     * Sets the durability of the item.
     *
     * <p>The behavior of this method is undefined when an item does not have a data value associated with it.
     *
     * @param durability The desired durability of the item.
     * @return The updated {@link ItemBuilderBase} object.
     */
    @Nonnull
    ItemBuilderBase durability(int durability);

    /**
     * Returns the durability or data value of the item.
     *
     * @return The durability of the item.
     */
    int getDurability();

    /**
     * Sets the maximum possible durability of the item.
     *
     * <p>Does nothing if the maximum durability cannot be set on the item.
     *
     * @param maxDurability The desired maximum durability for the item.
     * @return The updated {@link ItemBuilderBase} object.
     */
    @Nonnull
    ItemBuilderBase maxDurability(int maxDurability);

    /**
     * Returns the maximum possible durability value of the item.
     *
     * <p>Returns zero if the item does not have a max damage value.
     *
     * @return The maximum durability of the item.
     */
    int getMaxDurability();

    /**
     * Adds the specified enchantment to the stack.
     *
     * <p>This method uses {@link ItemStack#addUnsafeEnchantment(Enchantment, int)} rather than
     * {@link ItemStack#addEnchantment(Enchantment, int)} to avoid the associated checks of whether level is within the
     * range for the enchantment.
     *
     * @param enchantment The enchantment to apply to the item.
     * @param level The level of the enchantment to apply to the item.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase enchant(@Nonnull Enchantment enchantment, int level);

    /**
     * Removes the specified enchantment from the stack.
     *
     * @param enchantment The enchantment to remove from the item.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase unenchant(@Nonnull Enchantment enchantment);

    /**
     * Accepts a variable number of {@link ItemFlag}s to apply to the stack.
     *
     * @param flag A variable-length argument containing the flags to be applied.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase flag(@Nonnull ItemFlag... flag);

    /**
     * Accepts a variable number of {@link ItemFlag}s to remove from the stack.
     *
     * @param flag A variable-length argument containing the flags to be removed.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase deflag(@Nonnull ItemFlag... flag);

    /**
     * If the item has {@link SkullMeta} (i.e. if the item is a skull), this can be used to set the skull's owner (i.e.
     * the player the skull represents.)
     *
     * <p>In older versions of the game, this also sets the skull's data value to 3 for 'player head', as setting the
     * skull's owner doesn't make much sense for the mob skulls. (This is irrelevant in later versions as the skull
     * owner can only be set on a PLAYER_HEAD item anyway).
     *
     * @param name The name of the player the skull item should resemble.
     * @return The {@link ItemBuilderBase} instance.
     * @deprecated use {@link #skullOwner(UUID)} instead.
     */
    @Nonnull
    @Deprecated
    ItemBuilderBase skullOwner(@Nullable String name);

    /**
     * If the item has {@link SkullMeta} (i.e. if the item is a skull), this can be used to set the skull's owner (i.e.
     * the player the skull represents.)
     *
     * <p>In older versions of the game, this also sets the skull's data value to 3 for 'player head', as setting the
     * skull's owner doesn't make much sense for the mob skulls. (This is irrelevant in later versions as the skull
     * owner can only be set on a PLAYER_HEAD item anyway).
     *
     * @param uuid The UUID of the player the skull item should resemble.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    ItemBuilderBase skullOwner(@Nullable UUID uuid);

    /**
     * This is used to, inline, perform an operation if a given condition is true.
     *
     * <p>This can be useful when applying stream operations to {@link ItemBuilderBase}s.
     *
     * <p>The {@link ItemBuilderBase} instance is supplied to both the predicate (condition) and result function.
     *
     * <p>For example:
     *
     * <pre>{@code
     * // Renames the ItemStack, if and only if, the stack's type is Acacia Doors.
     * ifThen(stack -> stack.getType() == Material.ACACIA_DOOR, stack -> stack.name("&aMagic Door"));
     * }</pre>
     *
     * @param ifTrue The condition upon which, <code>then</code> should be performed.
     * @param then The action to perform if the predicate, <code>ifTrue</code>, is true.
     * @return The {@link ItemBuilderBase} instance.
     */
    @Nonnull
    default ItemBuilderBase ifThen(Predicate<ItemBuilderBase> ifTrue, Consumer<ItemBuilderBase> then) {
        if (ifTrue.test(this)) then.accept(this);
        return this;
    }

    /**
     * Returns the {@link ItemStack} that has been configured on the {@link ItemBuilderBase}.
     *
     * @return The manipulated ItemStack.
     */
    @Nonnull
    ItemStack build();

    /**
     * An alias for {@link #build()}.
     *
     * @return the built item stack.
     * @see #build()
     */
    @Nonnull
    default ItemStack get() {
        return build();
    }
}
