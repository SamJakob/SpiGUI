package com.samjakob.spigui.item;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A legacy (pre-1.13) Minecraft implementation of the {@link ItemBuilderBase} that uses the old metadata fields (e.g.,
 * data/durability/damage values).
 */
@SuppressWarnings({"RedundantSuppression", "deprecation"})
public final class LegacyItemBuilder implements ItemBuilderBase {

    /** Ensures that the {@link LegacyItemBuilder} has been registered on the {@link ItemBuilderFactory}. */
    public static void register() {
        if (!ItemBuilderFactory.get().hasConstructors()) {
            ItemBuilderFactory.get().setConstructors(new ItemBuilderConstructors() {
                @Override
                public ItemBuilderBase create(@Nonnull Material material) {
                    return new LegacyItemBuilder(material);
                }

                @Override
                public ItemBuilderBase from(@Nonnull ItemStack stack) {
                    return new LegacyItemBuilder(stack);
                }
            });
        }
    }

    /** The item stack being built. */
    private final ItemStack stack;

    /**
     * Constructor for creating a new ItemBuilder with a new internal stack derived from the given {@link Material}.
     *
     * @param material to create the new stack for.
     * @see ItemBuilderBase#create(Material)
     */
    private LegacyItemBuilder(@Nonnull Material material) {
        validateMaterial(material);
        this.stack = new ItemStack(material);
    }

    /**
     * Constructor for creating the ItemBuilder with an initial configuration derived from an {@link ItemStack}.
     *
     * <p><b>NOTE:</b> contrary to before, this method now clones the {@link ItemStack} to ensure that the reference
     * passed in is not unexpectedly mutated.
     *
     * @param stack to create the {@link LegacyItemBuilder} from.
     * @see ItemBuilderBase#from(ItemStack)
     */
    private LegacyItemBuilder(@Nonnull ItemStack stack) {
        validateMaterial(stack.getType());
        this.stack = stack.clone();
    }

    /**
     * Ensures that the specified {@link Material} is a valid material.
     *
     * @param material to check.
     */
    private void validateMaterial(Material material) {
        if (material == Material.AIR) {
            throw new IllegalArgumentException(
                    String.format("Cannot create ItemBuilder for invalid stack type: %s", stack.getType()));
        }
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
    public ItemBuilderBase name(@Nullable String name) {
        ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.setDisplayName(name != null ? ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name) : null);
        stack.setItemMeta(meta);
        return this;
    }

    @Nullable
    @Override
    public String getName() {
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        if (!meta.hasDisplayName()) return null;
        return meta.getDisplayName();
    }

    @Nonnull
    @Override
    public ItemBuilderBase amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    @Override
    public int getAmount() {
        return stack.getAmount();
    }

    @Nonnull
    @Override
    public ItemBuilderBase lore(@Nullable final String... lore) {
        return lore(lore != null ? Arrays.asList(lore) : null);
    }

    @Nonnull
    @Override
    public ItemBuilderBase lore(@Nullable final List<String> lore) {
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());

        if (lore != null) {
            meta.setLore(lore.stream()
                    .map(line -> line != null
                            // Handle color codes on each line.
                            ? ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', line)
                            // Replace null with an empty line.
                            : "")
                    .collect(Collectors.toList()));
        } else {
            meta.setLore(null);
        }

        stack.setItemMeta(meta);
        return this;
    }

    @Nullable
    @Override
    public List<String> getLore() {
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        if (!meta.hasLore()) return null;
        return meta.getLore();
    }

    @Nonnull
    @Override
    public ItemBuilderBase color(@Nonnull ItemColor color) {
        return durability(
                LegacyItemDataColor.getByColor(Objects.requireNonNull(color)).getDurability());
    }

    @Nonnull
    @Override
    public ItemBuilderBase data(short data) {
        return durability(data);
    }

    @Nonnull
    @Override
    public ItemBuilderBase durability(int durability) {
        final int maxDurability = getMaxDurability();
        if (durability > maxDurability) {
            throw new IllegalArgumentException(String.format(
                    "Invalid durability (%d). Exceeds maximum permitted value of (%d)", durability, maxDurability));
        }

        stack.setDurability((short) durability);
        return this;
    }

    @Override
    public int getDurability() {
        return stack.getDurability();
    }

    @Nonnull
    @Override
    public ItemBuilderBase maxDurability(int maxDurability) {
        /* no-op */
        return this;
    }

    @Override
    public int getMaxDurability() {
        return Short.MAX_VALUE;
    }

    @Nullable
    @Override
    public ItemColor getColor() {
        return Optional.ofNullable(LegacyItemDataColor.getByDurability(stack.getDurability()))
                .map(LegacyItemDataColor::getColor)
                .orElse(null);
    }

    @Nonnull
    @Override
    public ItemBuilderBase enchant(@Nonnull Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase unenchant(@Nonnull Enchantment enchantment) {
        stack.removeEnchantment(enchantment);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase flag(@Nonnull ItemFlag... flag) {
        ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase deflag(@Nonnull ItemFlag... flag) {
        ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.removeItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable String name) {
        ItemMeta itemMeta = Objects.requireNonNull(stack.getItemMeta());
        if (!(itemMeta instanceof SkullMeta)) return this;
        final SkullMeta meta = (SkullMeta) itemMeta;
        stack.setDurability((byte) 3);
        meta.setOwner(name);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable UUID uuid) {
        final ItemMeta itemMeta = Objects.requireNonNull(stack.getItemMeta());
        if (!(itemMeta instanceof SkullMeta)) return this;

        final SkullMeta meta = (SkullMeta) itemMeta;
        stack.setDurability((byte) 3);

        if (uuid == null) {
            meta.setOwner(null);
        } else {
            meta.setOwner(Objects.requireNonNull(
                            Bukkit.getServer().getOfflinePlayer(uuid),
                            String.format("Unrecognized player UUID: %s", uuid))
                    .getName());
        }

        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemStack build() {
        return stack.clone();
    }
}
