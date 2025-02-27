package com.samjakob.spigui.item;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A modern (1.13+) Minecraft implementation of the {@link ItemBuilderBase} that uses new fields and APIs to customize
 * the item stacks.
 */
@SuppressWarnings({"RedundantSuppression", "deprecation"})
public final class ModernItemBuilder implements ItemBuilderBase {

    /** Ensures that the {@link ModernItemBuilder} has been registered on the {@link ItemBuilderFactory}. */
    public static void register() {
        if (!ItemBuilderFactory.get().hasConstructors()) {
            ItemBuilderFactory.get().setConstructors(new ItemBuilderConstructors() {
                @Override
                public ItemBuilderBase create(@Nonnull Material material) {
                    return new ModernItemBuilder(material);
                }

                @Override
                public ItemBuilderBase from(@Nonnull ItemStack stack) {
                    return new ModernItemBuilder(stack);
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
    private ModernItemBuilder(@Nonnull Material material) {
        validateMaterial(material);
        this.stack = new ItemStack(material);
    }

    /**
     * Constructor for creating the ItemBuilder with an initial configuration derived from an {@link ItemStack}.
     *
     * <p><b>NOTE:</b> contrary to before, this method now clones the {@link ItemStack} to ensure that the reference
     * passed in is not unexpectedly mutated.
     *
     * @param stack to create the {@link ModernItemBuilder} from.
     * @see ItemBuilderBase#from(ItemStack)
     */
    private ModernItemBuilder(@Nonnull ItemStack stack) {
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
    public ItemBuilderBase type(@Nonnull Material material) {
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
        final var meta = Objects.requireNonNull(stack.getItemMeta());
        meta.setDisplayName(name != null ? ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name) : null);
        stack.setItemMeta(meta);
        return this;
    }

    @Nullable
    @Override
    public String getName() {
        final var meta = Objects.requireNonNull(stack.getItemMeta());
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
    public ItemBuilderBase lore(@Nullable String... lore) {
        return lore(lore != null ? Arrays.asList(lore) : null);
    }

    @Nonnull
    @Override
    public ItemBuilderBase lore(@Nullable List<String> lore) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

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
        final var meta = Objects.requireNonNull(stack.getItemMeta());
        if (!meta.hasLore()) return null;
        return meta.getLore();
    }

    @Nonnull
    @Override
    public ItemBuilderBase data(short data) {
        throw new UnsupportedOperationException(
                "#data(short) is not implemented for Minecraft 1.13+. If you intended to set the color of a colored block, use #color, for durability use #durability. If there's something you were expecting to use this for that is no longer available, please open a GitHub issue.");
    }

    @Nullable
    @Override
    public ItemColor getColor() {
        // Find a color where the type starts with COLOR_NAME_ (e.g., WHITE_).
        return Arrays.stream(ItemColor.values())
                .filter(color -> getType().name().startsWith(color.name().concat("_")))
                .findAny()
                .orElse(null);
    }

    @Nonnull
    @Override
    public ItemBuilderBase color(@Nonnull ItemColor color) {
        // If we cannot identify the current type as a colored type, do nothing.
        final var currentColor = getColor();
        if (currentColor == null) return this;

        // Attempt to swap the prefix for the other color.
        final var currentColorPrefix = String.format("%s_", currentColor);
        final String newTypeName = getType().name().replace(currentColorPrefix, String.format("%s_", color.name()));

        // If the swapped prefix is still a valid type, change the type.
        // Otherwise, do nothing.
        try {
            final Material newType = Material.valueOf(newTypeName);
            return this.type(newType);
        } catch (IllegalArgumentException ignored) {
            return this;
        }
    }

    @Nonnull
    @Override
    public ItemBuilderBase durability(int durability) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof Damageable damageable) {
            // If there's a limit, check that we're under it.
            if (damageable.hasMaxDamage()) {
                final int maxDurability = getMaxDurability();
                if (durability > maxDurability) {
                    throw new IllegalArgumentException(String.format(
                            "Invalid durability (%d). Exceeds maximum permitted value of (%d)",
                            durability, maxDurability));
                }
            }

            damageable.setDamage(durability);
        }

        return this;
    }

    @Override
    public int getDurability() {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof Damageable damageable) {
            return (short) damageable.getDamage();
        }

        return 0;
    }

    @Nonnull
    @Override
    public ItemBuilderBase maxDurability(int maxDurability) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof Damageable damageable) {
            if (damageable.hasMaxDamage()) {
                damageable.setMaxDamage(maxDurability);
            }
        }

        return this;
    }

    @Override
    public int getMaxDurability() {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof Damageable damageable) {
            if (damageable.hasMaxDamage()) {
                return (short) damageable.getMaxDamage();
            }
        }

        return 0;
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
        final var meta = Objects.requireNonNull(stack.getItemMeta());
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase deflag(@Nonnull ItemFlag... flag) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());
        meta.removeItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable String name) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof SkullMeta skullMeta) {
            if (name != null) {
                skullMeta.setOwningPlayer(
                        Objects.requireNonNull(Bukkit.getServer().getPlayerExact(name)));
            } else {
                skullMeta.setOwningPlayer(null);
            }
        }

        stack.setItemMeta(meta);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable UUID uuid) {
        final var meta = Objects.requireNonNull(stack.getItemMeta());

        if (meta instanceof SkullMeta skullMeta) {
            if (uuid != null) {
                skullMeta.setOwningPlayer(
                        Objects.requireNonNull(Bukkit.getServer().getOfflinePlayer(uuid)));
            } else {
                skullMeta.setOwningPlayer(null);
            }
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
