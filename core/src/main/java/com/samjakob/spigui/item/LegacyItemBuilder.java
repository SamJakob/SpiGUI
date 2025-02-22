package com.samjakob.spigui.item;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
 * A legacy (pre-1.13) Minecraft implementation of the {@link ItemBuilder} that uses the old metadata fields (e.g.,
 * data/durability/damage values).
 */
@SuppressWarnings("deprecation")
public class LegacyItemBuilder implements ItemBuilder {

    /** The item stack being built. */
    private final ItemStack stack;

    /**
     * Constructor for creating a new ItemBuilder with a new internal stack derived from the given {@link Material}.
     *
     * @param material to create the new stack for.
     * @see ItemBuilder#create(Material)
     */
    public LegacyItemBuilder(@Nonnull Material material) {
        this.stack = new ItemStack(material);
    }

    /**
     * Constructor for creating the ItemBuilder with an initial configuration derived from an {@link ItemStack}.
     *
     * @param stack to create the {@link LegacyItemBuilder} from.
     * @see ItemBuilder#from(ItemStack)
     */
    public LegacyItemBuilder(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    @Nonnull
    @Override
    public LegacyItemBuilder type(@Nonnull Material material) {
        stack.setType(material);
        return this;
    }

    @Nonnull
    @Override
    public Material getType() {
        return stack.getType();
    }

    @Nonnull
    @Override
    public ItemBuilder name(@Nullable String name) {
        ItemMeta stackMeta = Objects.requireNonNull(stack.getItemMeta());
        stackMeta.setDisplayName(
                name != null ? ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name) : null);
        stack.setItemMeta(stackMeta);
        return this;
    }

    @Nullable @Override
    public String getName() {
        final var stackMeta = Objects.requireNonNull(stack.getItemMeta());
        if (!stackMeta.hasDisplayName()) return null;
        return stackMeta.getDisplayName();
    }

    @Nonnull
    @Override
    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    @Override
    public int getAmount() {
        return stack.getAmount();
    }

    @Nonnull
    @Override
    public ItemBuilder lore(@Nullable final String... lore) {
        return lore(lore != null ? Arrays.asList(lore) : null);
    }

    @Nonnull
    @Override
    public ItemBuilder lore(@Nullable final List<String> lore) {
        ItemMeta stackMeta = Objects.requireNonNull(stack.getItemMeta());

        if (lore != null) {
            stackMeta.setLore(lore.stream()
                    .map(line -> line != null
                            // Handle color codes on each line.
                            ? ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', line)
                            // Replace null with an empty line.
                            : "")
                    .toList());
        } else {
            stackMeta.setLore(null);
        }

        stack.setItemMeta(stackMeta);
        return this;
    }

    @Nullable @Override
    public List<String> getLore() {
        final var stackMeta = Objects.requireNonNull(stack.getItemMeta());
        if (!stackMeta.hasLore()) return null;
        return stackMeta.getLore();
    }

    @Nonnull
    @Override
    public ItemBuilder color(@Nonnull ItemDataColor color) {
        return durability(color.getValue());
    }

    @Nonnull
    @Override
    public ItemBuilder data(short data) {
        return durability(data);
    }

    @Nonnull
    @Override
    public ItemBuilder durability(short durability) {
        stack.setDurability(durability);
        return this;
    }

    @Override
    public short getDurability() {
        return stack.getDurability();
    }

    @Nullable @Override
    public ItemDataColor getColor() {
        return ItemDataColor.getByValue(stack.getDurability());
    }

    @Nonnull
    @Override
    public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder unenchant(@Nonnull Enchantment enchantment) {
        stack.removeEnchantment(enchantment);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder flag(@Nonnull ItemFlag... flag) {
        ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder deflag(@Nonnull ItemFlag... flag) {
        ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.removeItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilder skullOwner(@Nullable String name) {
        ItemMeta itemMeta = Objects.requireNonNull(stack.getItemMeta());
        if (!(itemMeta instanceof SkullMeta meta)) return this;

        stack.setDurability((byte) 3);
        meta.setOwner(name);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemStack build() {
        return stack;
    }
}
