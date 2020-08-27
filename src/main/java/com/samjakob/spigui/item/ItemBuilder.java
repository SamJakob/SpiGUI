package com.samjakob.spigui.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A helper class for creating or modifying ItemStacks.
 *
 * The class wraps an ItemStack object and provides convenient chainable, 'builder-pattern' methods for
 * manipulating the stack's metadata.
 *
 * The intention is that this class will be used in builder form - for example;
 * <pre>
 * new ItemBuilder(Material.SPONGE).name("&amp;cAlmighty sponge").amount(21).build();
 * </pre>
 *
 * @author SamJakob
 * @version 2.0.0
 * @see ItemStack
 */
public class ItemBuilder {

    private final ItemStack stack;

    /* CONSTRUCT */

    /**
     * Creates an {@link ItemStack} and {@link ItemBuilder} wrapper for a new stack with the
     * given type.
     *
     * @param material The {@link Material} to use when creating the stack.
     */
    public ItemBuilder (Material material) {
        this.stack = new ItemStack(material);
    }

    /**
     * Creates an ItemBuilder wrapper for a given stack.
     * @param stack The ItemStack to wrap.
     */
    public ItemBuilder (ItemStack stack) {
        this.stack = stack;
    }

    /* MANIPULATE / READ */

    /**
     * Sets the type ({@link Material}) of the ItemStack.
     *
     * @param material The {@link Material} of the stack.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder type(Material material) {
        stack.setType(material);
        return this;
    }

    /**
     * Returns the type ({@link Material}) of the ItemStack.
     *
     * @return The {@link Material} of the stack.
     */
    public Material getType() {
        return stack.getType();
    }

    /**
     * Sets the display name of the item.
     * Color codes using the ampersand (&amp;) are translated, if you want to avoid this,
     * you should wrap your name argument with a {@link ChatColor#stripColor(String)} call.
     *
     * @param name The desired display name of the item stack.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder name(String name) {
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Returns either the display name of the item, if it exists, or null if it doesn't.
     *
     * You should note that this method fetches the name directly from the stack's {@link ItemMeta},
     * so you should take extra care when comparing names with color codes - particularly if you used the
     * {@link #name(String)} method as they will be in their translated sectional symbol (§) form,
     * rather than their 'coded' form (&amp;).
     *
     * For example, if you used {@link #name(String)} to set the name to '&amp;cMy Item', the output of this
     * method would be '§cMy Item'
     *
     * @return The item's display name as returned from its {@link ItemMeta}.
     */
    public String getName() {
        if (!stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) return null;
        return stack.getItemMeta().getDisplayName();
    }

    /**
     * Sets the amount of items in the {@link ItemStack}.
     *
     * @param amount The new amount.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    /**
     * Returns the amount of items in the {@link ItemStack}.
     *
     * @return The amount of items in the stack.
     */
    public int getAmount() {
        return stack.getAmount();
    }

    /**
     * Sets the lore of the item. This method is a var-args alias for the
     * {@link #lore(List)} method.
     *
     * @param lore The desired lore of the item, with each line as a separate string.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    /**
     * Sets the lore of the item.
     * As with {@link #name(String)}, color codes will be replaced. Each string represents
     * a line of the lore.
     *
     * Lines will not be automatically wrapped or truncated, so it is recommended you take
     * some consideration into how the item will be rendered with the lore.
     *
     * @param lore The desired lore of the item, with each line as a separate string.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder lore(List<String> lore) {
        for(int i = 0; i < lore.size(); i++){
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(lore);
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Gets the lore of the item as a list of strings. Each string represents a line of the
     * item's lore in-game.
     *
     * As with {@link #name(String)} it should be note that color-coded lore lines will
     * be returned with the colors codes already translated.
     *
     * @return The lore of the item.
     */
    public List<String> getLore() {
        if (!stack.hasItemMeta() || !stack.getItemMeta().hasLore()) return null;
        return stack.getItemMeta().getLore();
    }

    /**
     * An alias for {@link #durability(short)} that takes an {@link ItemDataColor} as an
     * argument instead. This is to improve code readability when working with items such
     * as glass panes, where the data value represents a glass pane's color.
     *
     * This method will still be functional for items where the data value does not represent
     * the item's color, however it will obviously be nonsensical.
     *
     * @param color The desired color of the item.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder color(ItemDataColor color) {
        return durability(color.getValue());
    }

    /**
     * An alias for {@link #durability(short)}.
     *
     * @param data The desired data-value (durability) of the item.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder data(short data) {
        return durability(data);
    }

    /**
     * Sets the durability (data value) of the item.
     *
     * @param durability The desired durability of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder durability(short durability) {
        stack.setDurability(durability);
        return this;
    }

    /**
     * Returns the durability or data value of the item.
     *
     * @return The durability of the item.
     */
    public short getDurability() {
        return stack.getDurability();
    }

    /**
     * Essentially a proxy for {@link ItemDataColor#getByValue(short)}.
     *
     * Similar to {@link #getDurability()} however it returns the value as an {@link ItemDataColor}
     * where it is applicable, or null where it isn't.
     *
     * @return The appropriate {@link ItemDataColor} of the item or null.
     */
    public ItemDataColor getColor() {
        return ItemDataColor.getByValue((short) stack.getDurability());
    }

    /**
     * Adds the specified enchantment to the stack.
     *
     * This method uses {@link ItemStack#addUnsafeEnchantment(Enchantment, int)} rather than {@link ItemStack#addEnchantment(Enchantment, int)}
     * to avoid the associated checks of whether level is within the range for the enchantment.
     *
     * @param enchantment The enchantment to apply to the item.
     * @param level The level of the enchantment to apply to the item.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Removes the specified enchantment from the stack.
     *
     * @param enchantment The enchantment to remove from the item.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder unenchant(Enchantment enchantment) {
        stack.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Accepts a variable number of {@link ItemFlag}s to apply to the stack.
     *
     * @param flag A variable-length argument containing the flags to be applied.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder flag(ItemFlag ...flag) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Accepts a variable number of {@link ItemFlag}s to remove from the stack.
     *
     * @param flag A variable-length argument containing the flags to be removed.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder deflag(ItemFlag ...flag) {
        ItemMeta meta = stack.getItemMeta();
        meta.removeItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * If the item has {@link SkullMeta} (i.e. if the item is a skull), this can
     * be used to set the skull's owner (i.e. the player the skull represents.)
     *
     * This also sets the skull's data value to 3 for 'player head', as setting
     * the skull's owner doesn't make much sense for the mob skulls.
     *
     * @param name The name of the player the skull item should resemble.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder skullOwner(String name) {
        if (!(stack.getItemMeta() instanceof SkullMeta)) return this;

        stack.setDurability((byte) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(name);
        stack.setItemMeta(meta);

        return this;
    }

    /* PREDICATE MANIPULATION */

    /**
     * This is used to, inline, perform an operation if a given condition is true.
     *
     * The {@link ItemBuilder} instance is supplied to both the predicate (condition) and result function.
     * The result of <code>then</code> is ignored as the ItemBuilder reference is passed to it.
     *
     * Example:
     * <pre>
     * // Renames the ItemStack, if and only if, the stack's type is Acacia Doors.
     * ifThen(stack -&gt; stack.getType() == Material.ACACIA_DOOR, stack -&gt; stack.name("&amp;aMagic Door"));
     * </pre>
     *
     * @param ifTrue The condition upon which, <code>then</code> should be performed.
     * @param then The action to perform if the predicate, <code>ifTrue</code>, is true.
     * @return The {@link ItemBuilder} instance.
     */
    public ItemBuilder ifThen(Predicate<ItemBuilder> ifTrue, Function<ItemBuilder, Object> then) {
        if (ifTrue.test(this))
            then.apply(this);

        return this;
    }

    /* BUILD */

    /**
     * An alias for {@link #get()}.
     * @return See {@link #get()}.
     */
    public ItemStack build() {
        return get();
    }

    /**
     * Returns the {@link ItemStack} that the {@link ItemBuilder} instance represents.
     *
     * The modifications are performed as they are called, so this method simply returns
     * the class's private stack field.
     *
     * @return The manipulated ItemStack.
     */
    public ItemStack get() {
        return stack;
    }

}
